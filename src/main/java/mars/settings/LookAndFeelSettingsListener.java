/*
Copyright (c) 2021,  Francesco Ferlin

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

import com.github.weisj.darklaf.LafManager;
import com.github.weisj.darklaf.settings.SettingsConfiguration;
import com.github.weisj.darklaf.settings.ThemeSettings;
import com.github.weisj.darklaf.theme.event.ThemeChangeEvent;
import com.github.weisj.darklaf.theme.event.ThemeChangeListener;
import com.github.weisj.darklaf.theme.info.AccentColorRule;
import com.github.weisj.darklaf.theme.info.FontSizeRule;
import mars.Globals;

import java.util.Arrays;

public class LookAndFeelSettingsListener implements ThemeChangeListener {

    private final ThemeSettings themeSettings = ThemeSettings.getInstance();

    public LookAndFeelSettingsListener() {
        SettingsConfiguration themeConfig = new SettingsConfiguration();

        final var themeName = Globals.getSettingsService().getSetting(Setting.Strings.LAF_THEME);

        themeConfig.setSystemPreferencesEnabled(Globals.getSettingsService().getSetting(Setting.Booleans.LAF_SYSTEM_PREFERENCES_ENABLED));
        themeConfig.setAccentColorFollowsSystem(Globals.getSettingsService().getSetting(Setting.Booleans.LAF_ACCENT_COLOR_FOLLOWS_SYSTEM));
        themeConfig.setSelectionColorFollowsSystem(Globals.getSettingsService().getSetting(Setting.Booleans.LAF_SELECTION_COLOR_FOLLOWS_SYSTEM));
        themeConfig.setFontSizeFollowsSystem(Globals.getSettingsService().getSetting(Setting.Booleans.LAF_FONT_SIZE_FOLLOWS_SYSTEM));
        themeConfig.setThemeFollowsSystem(Globals.getSettingsService().getSetting(Setting.Booleans.LAF_THEME_FOLLOWS_SYSTEM));

        themeConfig.setTheme(Arrays.stream(LafManager.getRegisteredThemes())
                .filter(theme -> theme.getName().equals(themeName))
                .findFirst()
                .orElse(themeSettings.getTheme()));

        try {
            themeConfig.setFontSizeRule(FontSizeRule.relativeAdjustment(Integer.parseInt(
                    Globals.getSettingsService().getSetting(Setting.Strings.LAF_FONT_SIZE))));
        } catch (NumberFormatException ex) {
            themeConfig.setFontSizeRule(themeSettings.getFontSizeRule());
        }

        // Workaround cause ThemeSettings doesn't correctly respect system accent color on its own
        // It needs help from ThemeSettingsUI.SettingsUIConfiguration#getAccentColorRule(), which this is based off of
        if(themeConfig.isSystemPreferencesEnabled()) {
            boolean useSystemAccent = themeConfig.isAccentColorFollowsSystem() && themeConfig.isSystemAccentColorSupported();
            boolean useSystemSelection = themeConfig.isSelectionColorFollowsSystem() && themeConfig.isSystemSelectionColorSupported();
            var themePreferredStyle = useSystemAccent || useSystemSelection ? LafManager.getPreferredThemeStyle() : null;
            themeConfig.setAccentColorRule(AccentColorRule.fromColor(
                    useSystemAccent ?
                            themePreferredStyle.getAccentColorRule().getAccentColor() :
                            Globals.getSettingsService().getSetting(Setting.Colors.LAF_ACCENT_COLOR),
                    useSystemSelection ?
                            themePreferredStyle.getAccentColorRule().getSelectionColor() :
                            Globals.getSettingsService().getSetting(Setting.Colors.LAF_SELECTION_COLOR)));
        }

        themeSettings.setConfiguration(themeConfig);
        themeSettings.peek();
    }

    @Override
    public void themeChanged(ThemeChangeEvent e) {
        if(!themeSettings.isSystemPreferencesEnabled() || !themeSettings.isThemeFollowsSystem())
            Globals.getSettingsService().setSetting(Setting.Strings.LAF_THEME, e.getNewTheme().getName());
        if(!themeSettings.isSystemPreferencesEnabled() || !themeSettings.isAccentColorFollowsSystem())
            Globals.getSettingsService().setSetting(Setting.Colors.LAF_ACCENT_COLOR, e.getNewTheme().getAccentColorRule().getAccentColor());
        if(!themeSettings.isSystemPreferencesEnabled() || !themeSettings.isSelectionColorFollowsSystem())
            Globals.getSettingsService().setSetting(Setting.Colors.LAF_SELECTION_COLOR, e.getNewTheme().getAccentColorRule().getSelectionColor());
        if(!themeSettings.isSystemPreferencesEnabled() || !themeSettings.isFontSizeFollowsSystem())
            Globals.getSettingsService().setSetting(Setting.Strings.LAF_FONT_SIZE, String.valueOf(e.getNewTheme().getFontSizeRule().getPercentage()));
    }

    @Override
    public void themeInstalled(ThemeChangeEvent e) {
        // Reset the service to reload the default values
        Globals.getSettingsService().reset();
    }
}
