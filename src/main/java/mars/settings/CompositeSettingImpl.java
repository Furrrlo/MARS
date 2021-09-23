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

import java.util.*;
import java.util.prefs.Preferences;

abstract class CompositeSettingImpl<T> implements SettingImpl<T> {

    protected final Collection<SettingImpl<?>> settings;

    CompositeSettingImpl(SettingImpl<?>... settings) {
        this(Arrays.asList(settings));
    }

    CompositeSettingImpl(Collection<SettingImpl<?>> settings) {
        this.settings = List.copyOf(settings);
    }

    protected abstract T parseFromValues(SettingToValue valueProvider);

    protected abstract Map<SettingImpl<?>, ?> writeToValues(T value);

    private T parseFromValues(Map<SettingImpl<?>, ?> values) {
        return parseFromValues(new SettingToValue() {
            @Override
            @SuppressWarnings("unchecked")
            public <E> E fromSetting(SettingImpl<E> setting) {
                return (E) values.get(setting);
            }
        });
    }

    interface SettingToValue {
        <E> E fromSetting(SettingImpl<E> setting);
    }

    @Override
    public T getDefaultValue() {
        final Map<SettingImpl<?>, Object> values = new HashMap<>(settings.size());
        settings.forEach(s -> values.put(s, s.getDefaultValue()));
        return parseFromValues(values);
    }

    @Override
    public T getValue() {
        final Map<SettingImpl<?>, Object> values = new HashMap<>(settings.size());
        settings.forEach(s -> values.put(s, s.getValue()));
        return parseFromValues(values);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void setValue(T value) {
        writeToValues(value).forEach((setting, component) -> ((SettingImpl<Object>) setting).setValue(component));
    }

    @Override
    public boolean isDefault() {
        return settings.stream().allMatch(SettingImpl::isDefault);
    }

    @Override
    public void setValueToDefault() {
        settings.forEach(SettingImpl::setValueToDefault);
    }

    @Override
    public void loadDefaultFromProperties(Properties properties) {
        settings.forEach(s -> s.loadDefaultFromProperties(properties));
    }

    @Override
    public void loadFromPreferences(Preferences preferences) {
        settings.forEach(s -> s.loadFromPreferences(preferences));
    }

    @Override
    public void writeToPreferences(Preferences preferences) {
        settings.forEach(s -> s.writeToPreferences(preferences));
    }

    @Override
    public void removeFromPreferences(Preferences preferences) {
        settings.forEach(s -> s.removeFromPreferences(preferences));
    }
}
