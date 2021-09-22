/*
Copyright (c) 2021,  Francesco Ferlin
Copyright (c) 2003-2013,  Pete Sanderson and Kenneth Vollmar

Developed by Pete Sanderson (psanderson@otterbein.edu)
and Kenneth Vollmar (kenvollmar@missouristate.edu)

Permission is hereby granted, free of charge, to any person obtaining
a copy of this software and associated documentation files (the
"Software"), to deal in the Software without restriction, including
without limitation the rights to use, copy, modify, merge, publish,
distribute, sublicense, and/or sell copies of the Software, and to
permit persons to whom the Software is furnished to do so, subject
to the following conditions:

The above copyright notice and this permission notice shall be
included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR
ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

(MIT license, http://www.opensource.org/licenses/mit-license.html)
 */
package mars.settings;

import mars.MarsLaunch;
import mars.util.PropertiesFile;

import java.util.Arrays;
import java.util.Objects;
import java.util.Observable;
import java.util.Properties;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 * Contains various IDE settings.  Persistent settings are maintained for the
 * current user and on the current machine using
 * Java's Preference objects.  Failing that, default setting values come from
 * Settings.properties file.  If both of those fail, default values come from
 * static arrays defined in this class.  The latter can can be modified prior to
 * instantiating Settings object.
 * <p>
 * NOTE: If the Preference objects fail due to security exceptions, changes to
 * settings will not carry over from one MARS session to the next.
 * <p>
 * Actual implementation of the Preference objects is platform-dependent.
 * For Windows, they are stored in Registry.  To see, run regedit and browse to:
 * HKEY_CURRENT_USER\Software\JavaSoft\Prefs\mars
 *
 * @author Pete Sanderson
 **/
public class SettingsService extends Observable {

    /* Properties file used to hold default settings. */
    private static final String SETTINGS_FILE = "Settings";

    private final Preferences preferences;

    /**
     * Create Settings object and set to saved values.  If saved values not found, will set
     * based on defaults stored in Settings.properties file.  If file problems, will set based
     * on defaults stored in this class.
     */
    public SettingsService() {
        // This determines where the values are actually stored.  Actual implementation
        // is platform-dependent.  For Windows, they are stored in Registry.  To see,
        // run regedit and browse to: HKEY_CURRENT_USER\Software\JavaSoft\Prefs\mars
        preferences = Preferences.userNodeForPackage(MarsLaunch.class);
        // The gui parameter, formerly passed to initialize(), is no longer needed
        // because I removed (1/21/09) the call to generate the Font object for the text editor.
        // Font objects are now generated only on demand so the "if (gui)" guard
        // is no longer necessary.  Originally added by Berkeley b/c they were running it on a
        // headless server and running in command mode.  The Font constructor resulted in Swing
        // initialization which caused problems.  Now this will only occur on demand from
        // Venus, which happens only when running as GUI.
        initialize();
    }

    /**
     * Initialize settings to default values.
     *
     * Strategy: First set from properties file.
     *           If that fails, set from array.
     *           In either case, use these values as defaults in call to Preferences.
     */
    private void initialize() {
        applyDefaultSettings();
        if (!readSettingsFromPropertiesFile(SETTINGS_FILE))
            System.out.println("MARS System error: unable to read Settings.properties defaults. Using built-in defaults.");
        getSettingsFromPreferences();
    }

    /** Default values.  Will be replaced if available from property file or Preferences object. */
    private void applyDefaultSettings() {
        Arrays.stream(Setting.values())
                .map(this::getSettingInternal)
                .forEach(SettingImpl::setValueToDefault);
    }

    /**
     * Establish the settings from the given properties file.  Return true if it worked,
     * false if it didn't.  Note the properties file exists only to provide default values
     * in case the Preferences fail or have not been recorded yet.
     * <p>
     * Any settings successfully read will be stored in both the xSettingsValues and
     * defaultXSettingsValues arrays (x=boolean,string,color).  The latter will overwrite the
     * last-resort default values hardcoded into the arrays above.
     *
     * @implNote if there is NO ENTRY for the specified property, Globals.getPropertyEntry() returns
     * null.  This is no cause for alarm.  It will occur during system development or upon the
     * first use of a new MARS release in which new settings have been defined.
     * In that case, this method will NOT make an assignment to the settings array!
     * So consider it a precondition of this method: the settings arrays must already be
     * initialized with last-resort default values.
     */
    private boolean readSettingsFromPropertiesFile(String filename) {
        try {
            final Properties properties = PropertiesFile.loadPropertiesFromFile(filename);
            Arrays.stream(Setting.values())
                    .map(this::getSettingInternal)
                    .forEach(s -> s.loadDefaultFromProperties(properties));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Get settings values from Preferences object.  A key-value pair will only be written
     * to Preferences if/when the value is modified.  If it has not been modified, the default value
     * will be returned here.
     *
     * PRECONDITION: Values arrays have already been initialized to default values from
     * Settings.properties file or default value arrays above!
     */
    private void getSettingsFromPreferences() {
        Arrays.stream(Setting.values())
                .map(this::getSettingInternal)
                .forEach(s -> s.loadFromPreferences(preferences));
    }

    /** Reset settings to default values, as described in the constructor comments. */
    public void reset() {
        initialize();
    }

    /**
     * Fetch value of a setting given its identifier.
     *
     * @param setting setting's identifier
     * @return corresponding setting.
     * @throws IllegalArgumentException if identifier is invalid.
     */
    public <T> T getSetting(Setting<T> setting) {
        return getSettingInternal(setting).getValue();
    }

    /**
     * Fetch the default value of a setting given its identifier.
     *
     * @param setting setting's identifier
     * @return corresponding setting.
     * @throws IllegalArgumentException if identifier is invalid.
     */
    public <T> T getDefaultSetting(Setting<T> setting) {
        return getSettingInternal(setting).getDefaultValue();
    }

    /**
     * Set default value of a setting given its id.
     *
     * @param setting setting's identifier
     * @throws IllegalArgumentException if identifier is not valid.
     */
    public <T> void setSettingToDefault(Setting<T> setting) {
        final var internalSetting = getSettingInternal(setting);
        if (!internalSetting.isDefault()) {
            internalSetting.setValueToDefault();
            saveSetting(internalSetting);
            setChanged();
            notifyObservers();
        }
    }

    /**
     * Set value of a setting given its id and the value.
     *
     * @param setting setting's identifier
     * @param value   value to store
     * @throws IllegalArgumentException if identifier is not valid.
     */
    public <T> void setSetting(Setting<T> setting, T value) {
        final var internalSetting = getSettingInternal(setting);
        if (value != internalSetting.getValue()) {
            internalSetting.setValue(value);
            saveSetting(internalSetting);
            setChanged();
            notifyObservers();
        }
    }

    /** Save the key-value pair in the Properties object and assure it is written to persisent storage. */
    private void saveSetting(SettingImpl<?> setting) {
        try {
            setting.writeToPreferences(preferences);
            preferences.flush();
        } catch (SecurityException se) {
            // cannot write to persistent storage for security reasons
        } catch (BackingStoreException bse) {
            // unable to communicate with persistent storage (strange days)
        }
    }

    /**
     * Temporarily establish setting. This setting will NOT be written to persisent
     * store!  Currently this is used only when running MARS from the command line
     *
     * @param setting setting's identifier
     * @param value   value to set
     */
    public <T> void setSettingNonPersistent(Setting<T> setting, T value) {
        getSettingInternal(setting).setValue(Objects.requireNonNull(value, "Setting value can't be null!"));
    }

    @SuppressWarnings("unchecked")
    private <T> SettingImpl<T> getSettingInternal(Setting<T> setting) {
        // This is p bad to look at, but enums so can't extend a package private class :I
        if(setting instanceof Setting.Booleans)
            return (SettingImpl<T>) ((Setting.Booleans) setting).settingImpl;
        if(setting instanceof Setting.Strings)
            return (SettingImpl<T>) ((Setting.Strings) setting).settingImpl;
        if(setting instanceof Setting.Fonts)
            return (SettingImpl<T>) ((Setting.Fonts) setting).settingImpl;
        if(setting instanceof Setting.Colors)
            return (SettingImpl<T>) ((Setting.Colors) setting).settingImpl;
        if(setting instanceof Setting.SyntaxStyles)
            return (SettingImpl<T>) ((Setting.SyntaxStyles) setting).settingImpl;
        throw new UnsupportedOperationException("You can't extend Setting yourself");
    }
}