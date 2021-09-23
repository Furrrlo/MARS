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

import mars.util.Binary;

import java.awt.*;
import java.util.Objects;

class ColorSettingImpl extends StringDerivedSettingImpl<Color> {

    public ColorSettingImpl(String key, String defaultValue) {
        super(key, new ColorStringSettingImpl(key, defaultValue));
    }

    public ColorSettingImpl(String key, int defaultValue) {
        this(key, argbToString(defaultValue));
    }

    public ColorSettingImpl(String key, int r, int g, int b) {
        this(key, rgbToString(r, g, b));
    }

    static String rgbToString(int r, int g, int b) {
        return Binary.intToHexString(r << 16 | g << 8 | b);
    }

    static String argbToString(int argb) {
        return  Binary.intToHexString(argb);
    }

    @Override
    protected Color parseFromString(String string) {
        try {
            return string == null ? null : Color.decode(string);
        } catch (NumberFormatException ignored) {
            return null;
        }
    }

    @Override
    protected String writeToString(Color value) {
        return value == null ?
                null :
                rgbToString(value.getRed(), value.getGreen(), value.getBlue());
    }

    private static class ColorStringSettingImpl extends StringSettingImpl {

        ColorStringSettingImpl(String key, String defaultValue) {
            super(key, defaultValue);
        }

        @Override
        public boolean isDefault() {
            // Comparing strings won't work properly,
            // but we still need to avoid using the Color class

            Integer currValue;
            try {
                final var sValue = getValue();
                currValue = sValue == null ? null : Integer.decode(sValue) & 0xFFFFFF;
            } catch (NumberFormatException ex) {
                currValue = null;
            }

            Integer defaultValue;
            try {
                final var sValue = getDefaultValue();
                defaultValue = sValue == null ? null : Integer.decode(sValue) & 0xFFFFFF;
            } catch (NumberFormatException ex) {
                defaultValue = null;
            }

            return Objects.equals(currValue, defaultValue);
        }
    }
}
