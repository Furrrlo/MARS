/*
Copyright (c) 2003-2008,  Pete Sanderson and Kenneth Vollmar

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
package mars.mips.dump;

import mars.util.FilenameFinder;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;


/****************************************************************************/
/* This class provides functionality to bring external memory dump format definitions
 * into MARS.  This is adapted from the ToolLoader class, which is in turn adapted
 * from Bret Barker's GameServer class from the book "Developing Games In Java".
 */
public class DumpFormatLoader {

    private static final String CLASS_PREFIX = "mars.mips.dump.";
    private static final String DUMP_DIRECTORY_PATH = "mars/mips/dump";
    private static final String SYSCALL_INTERFACE = "DumpFormat.class";
    private static final String CLASS_EXTENSION = "class";

    private static List<DumpFormat> formatList = null;

    public static DumpFormat findDumpFormatGivenCommandDescriptor(List<DumpFormat> formatList, String formatCommandDescriptor) {
        DumpFormat match = null;
        for (DumpFormat dumpFormat : formatList) {
            if (dumpFormat.getCommandDescriptor().equals(formatCommandDescriptor)) {
                match = dumpFormat;
                break;
            }
        }
        return match;
    }

    /**
     * Dynamically loads dump formats into an ArrayList.  This method is adapted from
     * the loadGameControllers() method in Bret Barker's GameServer class.
     * Barker (bret@hypefiend.com) is co-author of the book "Developing Games
     * in Java".  Also see the ToolLoader and SyscallLoader classes elsewhere in MARS.
     */
    public List<DumpFormat> loadDumpFormats() {
        // The list will be populated only the first time this method is called.
        if (formatList == null) {
            formatList = new ArrayList<>();
            // grab all class files in the dump directory
            List<String> candidates = FilenameFinder.getFilenameList(this.getClass().getClassLoader(),
                    DUMP_DIRECTORY_PATH, CLASS_EXTENSION);
            for (String file : candidates) {
                try {
                    // grab the class, make sure it implements DumpFormat, instantiate, add to list
                    String formatClassName = CLASS_PREFIX + file.substring(0, file.indexOf(CLASS_EXTENSION) - 1);
                    Class<?> clas = Class.forName(formatClassName);
                    if (DumpFormat.class.isAssignableFrom(clas) &&
                            !Modifier.isAbstract(clas.getModifiers()) &&
                            !Modifier.isInterface(clas.getModifiers())) {
                        formatList.add((DumpFormat) clas.getDeclaredConstructor().newInstance());
                    }
                } catch (Exception e) {
                    System.out.println("Error instantiating DumpFormat from file " + file + ": " + e);
                }
            }
        }
        return formatList;
    }
}
