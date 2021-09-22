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

import java.util.Properties;
import java.util.prefs.Preferences;

abstract class StringDerivedSettingImpl<T> implements SettingImpl<T> {

    private final SettingImpl<String> actualSetting;
    protected final String key;

    StringDerivedSettingImpl(String key, T defaultValue) {
        this.actualSetting = new CaseInsensitiveStringSettingImpl(key, writeToString(defaultValue));
        this.key = key;
    }

    StringDerivedSettingImpl(String key, String defaultValue) {
        this.actualSetting = new CaseInsensitiveStringSettingImpl(key, defaultValue);
        this.key = key;
    }

    StringDerivedSettingImpl(String key, SettingImpl<String> actualSetting) {
        this.actualSetting = actualSetting;
        this.key = key;
    }

    protected abstract T parseFromString(String string);

    protected abstract String writeToString(T value);

    @Override
    public T getDefaultValue() {
        return parseFromString(actualSetting.getDefaultValue());
    }

    @Override
    public T getValue() {
        return parseFromString(actualSetting.getValue());
    }

    @Override
    public void setValue(T value) {
        actualSetting.setValue(writeToString(value));
    }

    @Override
    public boolean isDefault() {
        return actualSetting.isDefault();
    }

    @Override
    public void setValueToDefault() {
        actualSetting.setValueToDefault();
    }

    @Override
    public void loadDefaultFromProperties(Properties properties) {
        actualSetting.loadDefaultFromProperties(properties);
    }

    @Override
    public void loadFromPreferences(Preferences preferences) {
        actualSetting.loadFromPreferences(preferences);
    }

    @Override
    public void writeToPreferences(Preferences preferences) {
        actualSetting.writeToPreferences(preferences);
    }

    @Override
    public void removeFromPreferences(Preferences preferences) {
        actualSetting.removeFromPreferences(preferences);
    }

    private static class CaseInsensitiveStringSettingImpl extends StringSettingImpl {

        CaseInsensitiveStringSettingImpl(String key, String defaultValue) {
            super(key, defaultValue);
        }

        @Override
        @SuppressWarnings("StringEquality")
        public boolean isDefault() {
            return (defaultValue == currentValue) || (defaultValue != null && defaultValue.equalsIgnoreCase(currentValue));
        }
    }
}
