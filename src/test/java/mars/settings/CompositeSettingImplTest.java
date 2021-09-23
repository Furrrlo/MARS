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

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CompositeSettingImplTest {

    private static class ConcatenateStringSetting extends CompositeSettingImpl<String> {

        private final SettingImpl<String> string1;
        private final SettingImpl<String> string2;

        private ConcatenateStringSetting(String string1, String string2) {
            this.string1 = new StringSettingImpl("TestString1", string1);
            this.string2 = new StringSettingImpl("TestString2", string2);
        }

        @Override
        protected String parseFromValues(SettingToValue valueProvider) {
            String s1 = valueProvider.fromSetting(string1);
            if(s1 == null)
                s1 = "null";
            String s2 = valueProvider.fromSetting(string1);
            if(s2 == null)
                s2 = "null";
            return s1 + s2;
        }

        @Override
        protected Map<SettingImpl<?>, ?> writeToValues(String value) {
            final Map<SettingImpl<?>, Object> map = new HashMap<>();
            map.put(string1, value);
            map.put(string2, value);
            return map;
        }
    }

    @Test
    void acceptsNull() {
        final CompositeSettingImpl<String> setting = new ConcatenateStringSetting(null, null);
        setting.setValue(null);
        assertEquals("nullnull", setting.getValue());
    }

    @Test
    void constructorAcceptsNull() {
        final CompositeSettingImpl<String> setting = new ConcatenateStringSetting(null, null);
        assertEquals("nullnull", setting.getValue());

        setting.setValue("test");

        setting.setValueToDefault();
        assertEquals("nullnull", setting.getValue());
    }

    @Test
    void isDefault() {
        final CompositeSettingImpl<String> setting = new ConcatenateStringSetting("yos", "yos");
        assertTrue(setting.isDefault());
        setting.setValue("yos");
        assertTrue(setting.isDefault());
    }
}