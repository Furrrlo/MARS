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

import java.util.prefs.Preferences;

abstract class SingleValueSettingImpl<T> implements SettingImpl<T> {

    protected final String key;

    protected T defaultValue;
    protected T currentValue;

    SingleValueSettingImpl(String key, T defaultValue) {
        this.key = key;
        this.defaultValue = defaultValue;
        this.currentValue = defaultValue;
    }

    @Override
    public T getDefaultValue() {
        return defaultValue;
    }

    @Override
    public T getValue() {
        return currentValue;
    }

    @Override
    public void setValue(T value) {
        this.currentValue = value;
    }

    @Override
    public void setValueToDefault() {
        this.currentValue = this.defaultValue;
    }

    @Override
    public void removeFromPreferences(Preferences preferences) {
        preferences.remove(key);
    }
}
