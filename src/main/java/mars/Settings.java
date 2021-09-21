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
package mars;

import mars.settings.Setting;
import mars.settings.SettingsService;
import mars.util.DelegatingObservable;
import mars.venus.editors.jeditsyntax.SyntaxStyle;

import java.awt.*;
import java.util.Arrays;
import java.util.Observable;
import java.util.StringTokenizer;

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
public class Settings extends DelegatingObservable {
    /**
     * Flag to determine whether or not program being assembled is limited to
     * basic MIPS instructions and formats.
     * @deprecated use {@link Setting.Booleans#EXTENDED_ASSEMBLER_ENABLED}
     */
    @Deprecated
    public static final int EXTENDED_ASSEMBLER_ENABLED = Setting.Booleans.EXTENDED_ASSEMBLER_ENABLED.getOldId();
    /////////////////////////////  PROPERTY ARRAY INDEXES /////////////////////////////
    // Because MARS is programmed to Java 1.4, we cannot use an enumerated type.

    // BOOLEAN SETTINGS...  
    /**
     * Flag to determine whether or not program being assembled is limited to
     * using register numbers instead of names. NOTE: Its default value is
     * false and the IDE provides no means to change it!
     * @deprecated use {@link Setting.Booleans#BARE_MACHINE_ENABLED}
     */
    @Deprecated
    public static final int BARE_MACHINE_ENABLED = Setting.Booleans.BARE_MACHINE_ENABLED.getOldId();
    /**
     * Flag to determine whether or not a file is immediately and automatically assembled
     * upon opening. Handy when using externa editor like mipster.
     * @deprecated use {@link Setting.Booleans#ASSEMBLE_ON_OPEN_ENABLED}
     */
    @Deprecated
    public static final int ASSEMBLE_ON_OPEN_ENABLED = Setting.Booleans.ASSEMBLE_ON_OPEN_ENABLED.getOldId();
    /**
     * Flag to determine whether only the current editor source file (enabled false) or
     * all files in its directory (enabled true) will be assembled when assembly is selected.
     * @deprecated use {@link Setting.Booleans#ASSEMBLE_ALL_ENABLED}
     */
    @Deprecated
    public static final int ASSEMBLE_ALL_ENABLED = Setting.Booleans.ASSEMBLE_ALL_ENABLED.getOldId();
    /**
     * Default visibilty of label window (symbol table).  Default only, dynamic status
     * maintained by ExecutePane
     * @deprecated use {@link Setting.Booleans#LABEL_WINDOW_VISIBILITY}
     */
    @Deprecated
    public static final int LABEL_WINDOW_VISIBILITY = Setting.Booleans.LABEL_WINDOW_VISIBILITY.getOldId();
    /**
     * Default setting for displaying addresses and values in hexidecimal in the Execute
     * pane.
     * @deprecated use {@link Setting.Booleans#DISPLAY_ADDRESSES_IN_HEX}
     */
    @Deprecated
    public static final int DISPLAY_ADDRESSES_IN_HEX = Setting.Booleans.DISPLAY_ADDRESSES_IN_HEX.getOldId();
    /**
     * @deprecated use {@link Setting.Booleans#DISPLAY_VALUES_IN_HEX}
     */
    @Deprecated
    public static final int DISPLAY_VALUES_IN_HEX = Setting.Booleans.DISPLAY_VALUES_IN_HEX.getOldId();
    /**
     * Flag to determine whether the currently selected exception handler source file will
     * be included in each assembly operation.
     * @deprecated use {@link Setting.Booleans#EXCEPTION_HANDLER_ENABLED}
     */
    @Deprecated
    public static final int EXCEPTION_HANDLER_ENABLED = Setting.Booleans.EXCEPTION_HANDLER_ENABLED.getOldId();
    /**
     * Flag to determine whether or not delayed branching is in effect at MIPS execution.
     * This means we simulate the pipeline and statement FOLLOWING a successful branch
     * is executed before branch is taken. DPS 14 June 2007.
     * @deprecated use {@link Setting.Booleans#DELAYED_BRANCHING_ENABLED}
     */
    @Deprecated
    public static final int DELAYED_BRANCHING_ENABLED = Setting.Booleans.DELAYED_BRANCHING_ENABLED.getOldId();
    /**
     * Flag to determine whether or not the editor will display line numbers.
     * @deprecated use {@link Setting.Booleans#EDITOR_LINE_NUMBERS_DISPLAYED}
     */
    @Deprecated
    public static final int EDITOR_LINE_NUMBERS_DISPLAYED = Setting.Booleans.EDITOR_LINE_NUMBERS_DISPLAYED.getOldId();
    /**
     * Flag to determine whether or not assembler warnings are considered errors.
     * @deprecated use {@link Setting.Booleans#WARNINGS_ARE_ERRORS}
     */
    @Deprecated
    public static final int WARNINGS_ARE_ERRORS = Setting.Booleans.WARNINGS_ARE_ERRORS.getOldId();
    /**
     * Flag to determine whether or not to display and use program arguments
     * @deprecated use {@link Setting.Booleans#PROGRAM_ARGUMENTS}
     */
    @Deprecated
    public static final int PROGRAM_ARGUMENTS = Setting.Booleans.PROGRAM_ARGUMENTS.getOldId();
    /**
     * Flag to control whether or not highlighting is applied to data segment window
     * @deprecated use {@link Setting.Booleans#DATA_SEGMENT_HIGHLIGHTING}
     */
    @Deprecated
    public static final int DATA_SEGMENT_HIGHLIGHTING = Setting.Booleans.DATA_SEGMENT_HIGHLIGHTING.getOldId();
    /**
     * Flag to control whether or not highlighting is applied to register windows
     * @deprecated use {@link Setting.Booleans#REGISTERS_HIGHLIGHTING}
     */
    @Deprecated
    public static final int REGISTERS_HIGHLIGHTING = Setting.Booleans.REGISTERS_HIGHLIGHTING.getOldId();
    /**
     * Flag to control whether or not assembler automatically initializes program counter to 'main's address
     * @deprecated use {@link Setting.Booleans#START_AT_MAIN}
     */
    @Deprecated
    public static final int START_AT_MAIN = Setting.Booleans.START_AT_MAIN.getOldId();
    /**
     * Flag to control whether or not editor will highlight the line currently being edited
     * @deprecated use {@link Setting.Booleans#EDITOR_CURRENT_LINE_HIGHLIGHTING}
     */
    @Deprecated
    public static final int EDITOR_CURRENT_LINE_HIGHLIGHTING = Setting.Booleans.EDITOR_CURRENT_LINE_HIGHLIGHTING.getOldId();
    /**
     * Flag to control whether or not editor will provide popup instruction guidance while typing
     * @deprecated use {@link Setting.Booleans#POPUP_INSTRUCTION_GUIDANCE}
     */
    @Deprecated
    public static final int POPUP_INSTRUCTION_GUIDANCE = Setting.Booleans.POPUP_INSTRUCTION_GUIDANCE.getOldId();
    /**
     * Flag to control whether or not simulator will use popup dialog for input syscalls
     * @deprecated use {@link Setting.Booleans#POPUP_SYSCALL_INPUT}
     */
    @Deprecated
    public static final int POPUP_SYSCALL_INPUT = Setting.Booleans.POPUP_SYSCALL_INPUT.getOldId();
    /**
     * Flag to control whether or not to use generic text editor instead of language-aware styled editor
     * @deprecated use {@link Setting.Booleans#GENERIC_TEXT_EDITOR}
     */
    @Deprecated
    public static final int GENERIC_TEXT_EDITOR = Setting.Booleans.GENERIC_TEXT_EDITOR.getOldId();
    /**
     * Flag to control whether or not language-aware editor will use auto-indent feature
     * @deprecated use {@link Setting.Booleans#AUTO_INDENT}
     */
    @Deprecated
    public static final int AUTO_INDENT = Setting.Booleans.AUTO_INDENT.getOldId();
    /**
     * Flag to determine whether a program can write binary code to the text or data segment and
     * execute that code.
     * @deprecated use {@link Setting.Booleans#SELF_MODIFYING_CODE_ENABLED}
     */
    @Deprecated
    public static final int SELF_MODIFYING_CODE_ENABLED = Setting.Booleans.SELF_MODIFYING_CODE_ENABLED.getOldId();

    // STRING SETTINGS.  Each array position has associated name.

    /**
     * Current specified exception handler file (a MIPS assembly source file)
     * @deprecated use {@link Setting.Strings#EXCEPTION_HANDLER}
     */
    @Deprecated
    public static final int EXCEPTION_HANDLER = Setting.Strings.EXCEPTION_HANDLER.getOldId();
    /**
     * Order of text segment table columns
     * @deprecated use {@link Setting.Strings#TEXT_COLUMN_ORDER}
     */
    @Deprecated
    public static final int TEXT_COLUMN_ORDER = Setting.Strings.TEXT_COLUMN_ORDER.getOldId();
    /**
     * State for sorting label window display
     * @deprecated use {@link Setting.Strings#LABEL_SORT_STATE}
     */
    @Deprecated
    public static final int LABEL_SORT_STATE = Setting.Strings.LABEL_SORT_STATE.getOldId();
    /**
     * Identifier of current memory configuration
     * @deprecated use {@link Setting.Strings#MEMORY_CONFIGURATION}
     */
    @Deprecated
    public static final int MEMORY_CONFIGURATION = Setting.Strings.MEMORY_CONFIGURATION.getOldId();
    /**
     * Caret blink rate in milliseconds, 0 means don't blink.
     * @deprecated use {@link Setting.Strings#CARET_BLINK_RATE}
     */
    @Deprecated
    public static final int CARET_BLINK_RATE = Setting.Strings.CARET_BLINK_RATE.getOldId();
    /**
     * Editor tab size in characters.
     * @deprecated use {@link Setting.Strings#EDITOR_TAB_SIZE}
     */
    @Deprecated
    public static final int EDITOR_TAB_SIZE = Setting.Strings.EDITOR_TAB_SIZE.getOldId();
    /**
     * Number of letters to be matched by editor's instruction guide before popup generated (if popup enabled)
     * @deprecated use {@link Setting.Strings#EDITOR_POPUP_PREFIX_LENGTH}
     */
    @Deprecated
    public static final int EDITOR_POPUP_PREFIX_LENGTH = Setting.Strings.EDITOR_POPUP_PREFIX_LENGTH.getOldId();

    // FONT SETTINGS.  Each array position has associated name.

    /**
     * Font for the text editor
     * @deprecated use {@link Setting.Fonts#EDITOR_FONT}
     */
    @Deprecated
    public static final int EDITOR_FONT = Setting.Fonts.EDITOR_FONT.getOldId();
    /**
     * Font for table even row background (text, data, register displays)
     * @deprecated use {@link Setting.Fonts#EVEN_ROW_FONT}
     */
    @Deprecated
    public static final int EVEN_ROW_FONT = Setting.Fonts.EVEN_ROW_FONT.getOldId();
    /**
     * Font for table odd row background (text, data, register displays)
     * @deprecated use {@link Setting.Fonts#ODD_ROW_FONT}
     */
    @Deprecated
    public static final int ODD_ROW_FONT = Setting.Fonts.ODD_ROW_FONT.getOldId();
    /**
     * Font for table odd row foreground (text, data, register displays)
     * @deprecated use {@link Setting.Fonts#TEXTSEGMENT_HIGHLIGHT_FONT}
     */
    @Deprecated
    public static final int TEXTSEGMENT_HIGHLIGHT_FONT = Setting.Fonts.TEXTSEGMENT_HIGHLIGHT_FONT.getOldId();
    /**
     * Font for text segment delay slot highlighted background
     * @deprecated use {@link Setting.Fonts#TEXTSEGMENT_DELAYSLOT_HIGHLIGHT_FONT}
     */
    @Deprecated
    public static final int TEXTSEGMENT_DELAYSLOT_HIGHLIGHT_FONT = Setting.Fonts.TEXTSEGMENT_DELAYSLOT_HIGHLIGHT_FONT.getOldId();
    /**
     * Font for text segment highlighted background
     * @deprecated use {@link Setting.Fonts#DATASEGMENT_HIGHLIGHT_FONT}
     */
    @Deprecated
    public static final int DATASEGMENT_HIGHLIGHT_FONT = Setting.Fonts.DATASEGMENT_HIGHLIGHT_FONT.getOldId();
    /**
     * Font for register highlighted background
     * @deprecated use {@link Setting.Fonts#REGISTER_HIGHLIGHT_FONT}
     */
    @Deprecated
    public static final int REGISTER_HIGHLIGHT_FONT = Setting.Fonts.REGISTER_HIGHLIGHT_FONT.getOldId();


    // COLOR SETTINGS.  Each array position has associated name.

    /**
     * RGB color for table even row background (text, data, register displays)
     * @deprecated use {@link Setting.Colors#EVEN_ROW_BACKGROUND}
     */
    @Deprecated
    public static final int EVEN_ROW_BACKGROUND = Setting.Colors.EVEN_ROW_BACKGROUND.getOldId();
    /**
     * RGB color for table even row foreground (text, data, register displays)
     * @deprecated use {@link Setting.Colors#EVEN_ROW_FOREGROUND}
     */
    @Deprecated
    public static final int EVEN_ROW_FOREGROUND = Setting.Colors.EVEN_ROW_FOREGROUND.getOldId();
    /**
     * RGB color for table odd row background (text, data, register displays)
     * @deprecated use {@link Setting.Colors#ODD_ROW_BACKGROUND}
     */
    @Deprecated
    public static final int ODD_ROW_BACKGROUND = Setting.Colors.ODD_ROW_BACKGROUND.getOldId();
    /**
     * RGB color for table odd row foreground (text, data, register displays)
     * @deprecated use {@link Setting.Colors#ODD_ROW_FOREGROUND}
     */
    @Deprecated
    public static final int ODD_ROW_FOREGROUND = Setting.Colors.ODD_ROW_FOREGROUND.getOldId();
    /**
     * RGB color for text segment highlighted background
     * @deprecated use {@link Setting.Colors#TEXTSEGMENT_HIGHLIGHT_BACKGROUND}
     */
    @Deprecated
    public static final int TEXTSEGMENT_HIGHLIGHT_BACKGROUND = Setting.Colors.TEXTSEGMENT_HIGHLIGHT_BACKGROUND.getOldId();
    /**
     * RGB color for text segment highlighted foreground
     * @deprecated use {@link Setting.Colors#TEXTSEGMENT_HIGHLIGHT_FOREGROUND}
     */
    @Deprecated
    public static final int TEXTSEGMENT_HIGHLIGHT_FOREGROUND = Setting.Colors.TEXTSEGMENT_HIGHLIGHT_FOREGROUND.getOldId();
    /**
     * RGB color for text segment delay slot highlighted background
     * @deprecated use {@link Setting.Colors#TEXTSEGMENT_DELAYSLOT_HIGHLIGHT_BACKGROUND}
     */
    @Deprecated
    public static final int TEXTSEGMENT_DELAYSLOT_HIGHLIGHT_BACKGROUND = Setting.Colors.TEXTSEGMENT_DELAYSLOT_HIGHLIGHT_BACKGROUND.getOldId();
    /**
     * RGB color for text segment delay slot highlighted foreground
     * @deprecated use {@link Setting.Colors#TEXTSEGMENT_DELAYSLOT_HIGHLIGHT_FOREGROUND}
     */
    @Deprecated
    public static final int TEXTSEGMENT_DELAYSLOT_HIGHLIGHT_FOREGROUND = Setting.Colors.TEXTSEGMENT_DELAYSLOT_HIGHLIGHT_FOREGROUND.getOldId();
    /**
     * RGB color for text segment highlighted background
     * @deprecated use {@link Setting.Colors#DATASEGMENT_HIGHLIGHT_BACKGROUND}
     */
    @Deprecated
    public static final int DATASEGMENT_HIGHLIGHT_BACKGROUND = Setting.Colors.DATASEGMENT_HIGHLIGHT_BACKGROUND.getOldId();
    /**
     * RGB color for text segment highlighted foreground
     * @deprecated use {@link Setting.Colors#DATASEGMENT_HIGHLIGHT_FOREGROUND}
     */
    @Deprecated
    public static final int DATASEGMENT_HIGHLIGHT_FOREGROUND = Setting.Colors.DATASEGMENT_HIGHLIGHT_FOREGROUND.getOldId();
    /**
     * RGB color for register highlighted background
     * @deprecated use {@link Setting.Colors#REGISTER_HIGHLIGHT_BACKGROUND}
     */
    @Deprecated
    public static final int REGISTER_HIGHLIGHT_BACKGROUND = Setting.Colors.REGISTER_HIGHLIGHT_BACKGROUND.getOldId();
    /**
     * RGB color for register highlighted foreground
     * @deprecated use {@link Setting.Colors#REGISTER_HIGHLIGHT_FOREGROUND}
     */
    @Deprecated
    public static final int REGISTER_HIGHLIGHT_FOREGROUND = Setting.Colors.REGISTER_HIGHLIGHT_FOREGROUND.getOldId();

    private final SettingsService newService;

    /**
     * Create Settings object and set to saved values.  If saved values not found, will set
     * based on defaults stored in Settings.properties file.  If file problems, will set based
     * on defaults stored in this class.
     */
    public Settings() {
        this(new SettingsService());
    }

    /**
     * Create Settings object and set to saved values.  If saved values not found, will set
     * based on defaults stored in Settings.properties file.  If file problems, will set based
     * on defaults stored in this class.
     *
     * @param gui true if running the graphical IDE, false if running from command line.
     *            Ignored as of release 3.6 but retained for compatibility.
     */
    public Settings(@SuppressWarnings("unused") boolean gui) {
        this(new SettingsService());
    }

    protected Settings(SettingsService newService) {
        super(newService);
        this.newService = newService;
    }

    /**
     * Return whether backstepping is permitted at this time.  Backstepping is ability to undo execution
     * steps one at a time.  Available only in the IDE.  This is not a persistent setting and is not under
     * MARS user control.
     *
     * @return true if backstepping is permitted, false otherwise.
     */
    public boolean getBackSteppingEnabled() {
        return (Globals.program != null && Globals.program.getBackStepper() != null && Globals.program.getBackStepper().enabled());
    }

    /**
     * Reset settings to default values, as described in the constructor comments.
     *
     * @param gui true if running from GUI IDE and false if running from command mode.
     *            Ignored as of release 3.6 but retained for compatibility.
     */
    public void reset(@SuppressWarnings("unused") boolean gui) {
        newService.reset();
    }

    public void setEditorSyntaxStyleByPosition(int index, SyntaxStyle syntaxStyle) {
        newService.setSetting(Setting.SyntaxStyles.TOKEN_SYNTAX_STYLES[index], syntaxStyle);
    }

    public SyntaxStyle getEditorSyntaxStyleByPosition(int index) {
        return newService.getSetting(Setting.SyntaxStyles.TOKEN_SYNTAX_STYLES[index]);
    }

    public SyntaxStyle getDefaultEditorSyntaxStyleByPosition(int index) {
        return newService.getDefaultSetting(Setting.SyntaxStyles.TOKEN_SYNTAX_STYLES[index]);
    }

    ////////////////////////////////////////////////////////////////////////
    //  Setting Getters
    ////////////////////////////////////////////////////////////////////////

    /**
     * Fetch value of a boolean setting given its identifier.
     *
     * @param id int containing the setting's identifier (constants listed above)
     * @return corresponding boolean setting.
     * @throws IllegalArgumentException if identifier is invalid.
     * @deprecated use {@link SettingsService#getSetting(Setting)}
     */
    public boolean getBooleanSetting(int id) {
        return newService.getSetting(Arrays.stream(Setting.Booleans.values())
                .filter(setting -> setting.getOldId() != null)
                .filter(setting -> setting.getOldId() == id)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid boolean setting ID")));
    }

    /**
     * Setting for whether user programs limited to "bare machine" formatted basic instructions.
     * This was added 8 Aug 2006 but is fixed at false for now, due to uncertainty as to what
     * exactly constitutes "bare machine".
     *
     * @return true if only bare machine instructions allowed, false otherwise.
     * @deprecated Use <code>getBooleanSetting(int id)</code> with the appropriate boolean setting ID
     * (e.g. <code>Settings.BARE_MACHINE_ENABLED</code>)
     */
    @Deprecated
    public boolean getBareMachineEnabled() {
        return newService.getSetting(Setting.Booleans.BARE_MACHINE_ENABLED);
    }

    /**
     * Setting for whether user programs can use pseudo-instructions or extended addressing modes
     * or alternative instruction formats (all are implemented as pseudo-instructions).
     *
     * @return true if pseudo-instructions and formats permitted, false otherwise.
     * @deprecated Use <code>getBooleanSetting(int id)</code> with the appropriate boolean setting ID
     * (e.g. <code>Settings.EXTENDED_ASSEMBLER_ENABLED</code>)
     */
    @Deprecated
    public boolean getExtendedAssemblerEnabled() {
        return newService.getSetting(Setting.Booleans.EXTENDED_ASSEMBLER_ENABLED);
    }

    /**
     * Establish setting for whether or not pseudo-instructions and formats are permitted
     * in user programs.  User can change this setting via the IDE.  If setting changes,
     * new setting will be written to properties file.
     *
     * @param value True to permit, false otherwise.
     * @deprecated Use <code>setBooleanSetting(int id, boolean value)</code> with the appropriate boolean setting ID
     * (e.g. <code>Settings.EXTENDED_ASSEMBLER_ENABLED</code>)
     */
    @Deprecated
    public void setExtendedAssemblerEnabled(boolean value) {
        newService.setSetting(Setting.Booleans.EXTENDED_ASSEMBLER_ENABLED, value);
    }

    /**
     * Setting for whether selected program will be automatically assembled upon opening. This
     * can be useful if user employs an external editor such as MIPSter.
     *
     * @return true if file is to be automatically assembled upon opening and false otherwise.
     * @deprecated Use <code>getBooleanSetting(int id)</code> with the appropriate boolean setting ID
     * (e.g. <code>Settings.ASSEMBLE_ON_OPEN_ENABLED</code>)
     */
    @Deprecated
    public boolean getAssembleOnOpenEnabled() {
        return newService.getSetting(Setting.Booleans.ASSEMBLE_ON_OPEN_ENABLED);
    }

    /**
     * Establish setting for whether a file will be automatically assembled as soon as it
     * is opened.  This is handy for those using an external text editor such as Mipster.
     * If setting changes, new setting will be written to properties file.
     *
     * @param value True to automatically assemble, false otherwise.
     * @deprecated Use <code>setBooleanSetting(int id, boolean value)</code> with the appropriate boolean setting ID
     * (e.g. <code>Settings.ASSEMBLE_ON_OPEN_ENABLED</code>)
     */
    @Deprecated
    public void setAssembleOnOpenEnabled(boolean value) {
        newService.setSetting(Setting.Booleans.ASSEMBLE_ON_OPEN_ENABLED, value);
    }

    /**
     * Setting for whether Addresses in the Execute pane will be displayed in hexadecimal.
     *
     * @return true if addresses are displayed in hexadecimal and false otherwise (decimal).
     * @deprecated Use <code>getBooleanSetting(int id)</code> with the appropriate boolean setting ID
     * (e.g. <code>Settings.DISPLAY_ADDRESSES_IN_HEX</code>)
     */
    @Deprecated
    public boolean getDisplayAddressesInHex() {
        return newService.getSetting(Setting.Booleans.DISPLAY_ADDRESSES_IN_HEX);
    }

    /**
     * Establish setting for whether addresses in the Execute pane will be displayed
     * in hexadecimal format.
     *
     * @param value True to display addresses in hexadecimal, false for decimal.
     * @deprecated Use <code>setBooleanSetting(int id, boolean value)</code> with the appropriate boolean setting ID
     * (e.g. <code>Settings.DISPLAY_ADDRESSES_IN_HEX</code>)
     */
    @Deprecated
    public void setDisplayAddressesInHex(boolean value) {
        newService.setSetting(Setting.Booleans.DISPLAY_ADDRESSES_IN_HEX, value);
    }

    /**
     * Setting for whether values in the Execute pane will be displayed in hexadecimal.
     *
     * @return true if values are displayed in hexadecimal and false otherwise (decimal).
     * @deprecated Use <code>getBooleanSetting(int id)</code> with the appropriate boolean setting ID
     * (e.g. <code>Settings.DISPLAY_VALUES_IN_HEX</code>)
     */
    @Deprecated
    public boolean getDisplayValuesInHex() {
        return newService.getSetting(Setting.Booleans.DISPLAY_VALUES_IN_HEX);
    }

    /**
     * Establish setting for whether values in the Execute pane will be displayed
     * in hexadecimal format.
     *
     * @param value True to display values in hexadecimal, false for decimal.
     * @deprecated Use <code>setBooleanSetting(int id, boolean value)</code> with the appropriate boolean setting ID
     * (e.g. <code>Settings.DISPLAY_VALUES_IN_HEX</code>)
     */
    @Deprecated
    public void setDisplayValuesInHex(boolean value) {
        newService.setSetting(Setting.Booleans.DISPLAY_VALUES_IN_HEX, value);
    }

    /**
     * Setting for whether the assemble operation applies only to the file currently open in
     * the editor or whether it applies to all files in that file's directory (primitive project
     * capability).  If the "assemble on open" setting is set, this "assemble all" setting will
     * be applied as soon as the file is opened.
     *
     * @return true if all files are to be assembled, false if only the file open in editor.
     * @deprecated Use <code>getBooleanSetting(int id)</code> with the appropriate boolean setting ID
     * (e.g. <code>Settings.ASSEMBLE_ALL_ENABLED</code>)
     */
    @Deprecated
    public boolean getAssembleAllEnabled() {
        return newService.getSetting(Setting.Booleans.ASSEMBLE_ALL_ENABLED);
    }

    /**
     * Establish setting for whether a file will be assembled by itself (false) or along
     * with all other files in its directory (true).  This permits multi-file programs
     * and a primitive "project" capability.  If setting changes,
     * new setting will be written to properties file.
     *
     * @param value True to assemble all, false otherwise.
     * @deprecated Use <code>setBooleanSetting(int id, boolean value)</code> with the appropriate boolean setting ID
     * (e.g. <code>Settings.ASSEMBLE_ALL_ENABLED</code>)
     */
    @Deprecated
    public void setAssembleAllEnabled(boolean value) {
        newService.setSetting(Setting.Booleans.ASSEMBLE_ALL_ENABLED, value);
    }

    /**
     * Setting for whether the currently selected exception handler
     * (a MIPS source file) will be automatically included in each
     * assemble operation.
     *
     * @return true if exception handler is to be included in assemble, false otherwise.
     * @deprecated Use <code>getBooleanSetting(int id)</code> with the appropriate boolean setting ID
     * (e.g. <code>Settings.EXCEPTION_HANDLER_ENABLED</code>)
     */
    @Deprecated
    public boolean getExceptionHandlerEnabled() {
        return newService.getSetting(Setting.Booleans.EXCEPTION_HANDLER_ENABLED);
    }

    /**
     * Establish setting for whether the currently selected exception handler
     * (a MIPS source file) will be automatically included in each
     * assemble operation. If setting changes, new setting will be written
     * to properties file.
     *
     * @param value True to assemble exception handler, false otherwise.
     * @deprecated Use <code>setBooleanSetting(int id, boolean value)</code> with the appropriate boolean setting ID
     * (e.g. <code>Settings.EXCEPTION_HANDLER_ENABLED</code>)
     */
    @Deprecated
    public void setExceptionHandlerEnabled(boolean value) {
        newService.setSetting(Setting.Booleans.EXCEPTION_HANDLER_ENABLED, value);
    }

    /**
     * Setting for whether delayed branching will be applied during MIPS
     * program execution.  If enabled, the statement following a successful
     * branch will be executed and then the branch is taken!  This simulates
     * pipelining and all MIPS processors do it.  However it is confusing to
     * assembly language students so is disabled by default.  SPIM does same thing.
     *
     * @return true if delayed branching is enabled, false otherwise.
     * @deprecated Use <code>getBooleanSetting(int id)</code> with the appropriate boolean setting ID
     * (e.g. <code>Settings.DELAYED_BRANCHING_ENABLED</code>)
     */
    @Deprecated
    public boolean getDelayedBranchingEnabled() {
        return newService.getSetting(Setting.Booleans.DELAYED_BRANCHING_ENABLED);
    }

    /**
     * Establish setting for whether delayed branching will be applied during
     * MIPS program execution.  If enabled, the statement following a successful
     * branch will be executed and then the branch is taken!  This simulates
     * pipelining and all MIPS processors do it.  However it is confusing to
     * assembly language students so is disabled by default.  SPIM does same thing.
     *
     * @param value True to enable delayed branching, false otherwise.
     * @deprecated Use <code>setBooleanSetting(int id, boolean value)</code> with the appropriate boolean setting ID
     * (e.g. <code>Settings.DELAYED_BRANCHING_ENABLED</code>)
     */
    @Deprecated
    public void setDelayedBranchingEnabled(boolean value) {
        newService.setSetting(Setting.Booleans.DELAYED_BRANCHING_ENABLED, value);
    }

    /**
     * Setting concerning whether or not to display the Labels Window -- symbol table.
     *
     * @return true if label window is to be displayed, false otherwise.
     * @deprecated Use <code>getBooleanSetting(int id)</code> with the appropriate boolean setting ID
     * (e.g. <code>Settings.LABEL_WINDOW_VISIBILITY</code>)
     */
    @Deprecated
    public boolean getLabelWindowVisibility() {
        return newService.getSetting(Setting.Booleans.LABEL_WINDOW_VISIBILITY);
    }

    /**
     * Establish setting for whether the labels window (i.e. symbol table) will
     * be displayed as part of the Text Segment display.  If setting changes,
     * new setting will be written to properties file.
     *
     * @param value True to dispay labels window, false otherwise.
     * @deprecated Use <code>setBooleanSetting(int id, boolean value)</code> with the appropriate boolean setting ID
     * (e.g. <code>Settings.LABEL_WINDOW_VISIBILITY</code>)
     */
    @Deprecated
    public void setLabelWindowVisibility(boolean value) {
        newService.setSetting(Setting.Booleans.LABEL_WINDOW_VISIBILITY, value);
    }

    /**
     * Setting concerning whether or not the editor will display line numbers.
     *
     * @return true if line numbers are to be displayed, false otherwise.
     * @deprecated Use <code>getBooleanSetting(int id)</code> with the appropriate boolean setting ID
     * (e.g. <code>Settings.EDITOR_LINE_NUMBERS_DISPLAYED</code>)
     */
    @Deprecated
    public boolean getEditorLineNumbersDisplayed() {
        return newService.getSetting(Setting.Booleans.EDITOR_LINE_NUMBERS_DISPLAYED);
    }

    /**
     * Establish setting for whether line numbers will be displayed by the
     * text editor.
     *
     * @param value True to display line numbers, false otherwise.
     * @deprecated Use <code>setBooleanSetting(int id, boolean value)</code> with the appropriate boolean setting ID
     * (e.g. <code>Settings.EDITOR_LINE_NUMBERS_DISPLAYED</code>)
     */
    @Deprecated
    public void setEditorLineNumbersDisplayed(boolean value) {
        newService.setSetting(Setting.Booleans.EDITOR_LINE_NUMBERS_DISPLAYED, value);
    }

    /**
     * Setting concerning whether or not assembler will consider warnings to be errors.
     *
     * @return true if warnings are considered errors, false otherwise.
     * @deprecated Use <code>getBooleanSetting(int id)</code> with the appropriate boolean setting ID
     * (e.g. <code>Settings.WARNINGS_ARE_ERRORS</code>)
     */
    @Deprecated
    public boolean getWarningsAreErrors() {
        return newService.getSetting(Setting.Booleans.WARNINGS_ARE_ERRORS);
    }

    /**
     * Establish setting for whether assembler warnings will be considered errors.
     *
     * @param value True to consider warnings to be errors, false otherwise.
     * @deprecated Use <code>setBooleanSetting(int id, boolean value)</code> with the appropriate boolean setting ID
     * (e.g. <code>Settings.WARNINGS_ARE_ERRORS</code>)
     */
    @Deprecated
    public void setWarningsAreErrors(boolean value) {
        newService.setSetting(Setting.Booleans.WARNINGS_ARE_ERRORS, value);
    }

    /**
     * Setting concerning whether or not program arguments can be entered and used.
     *
     * @return true if program arguments can be entered/used, false otherwise.
     * @deprecated Use <code>getBooleanSetting(int id)</code> with the appropriate boolean setting ID
     * (e.g. <code>Settings.PROGRAM_ARGUMENTS</code>)
     */
    @Deprecated
    public boolean getProgramArguments() {
        return newService.getSetting(Setting.Booleans.PROGRAM_ARGUMENTS);
    }

    /**
     * Establish setting for whether program arguments can be ented/used.
     *
     * @param value True if program arguments can be entered/used, false otherwise.
     * @deprecated Use <code>setBooleanSetting(int id, boolean value)</code> with the appropriate boolean setting ID
     * (e.g. <code>Settings.PROGRAM_ARGUMENTS</code>)
     */
    @Deprecated
    public void setProgramArguments(boolean value) {
        newService.setSetting(Setting.Booleans.PROGRAM_ARGUMENTS, value);
    }

    /**
     * Setting concerning whether or not highlighting is applied to Data Segment window.
     *
     * @return true if highlighting is to be applied, false otherwise.
     * @deprecated Use <code>getBooleanSetting(int id)</code> with the appropriate boolean setting ID
     * (e.g. <code>Settings.DATA_SEGMENT_HIGHLIGHTING</code>)
     */
    @Deprecated
    public boolean getDataSegmentHighlighting() {
        return newService.getSetting(Setting.Booleans.DATA_SEGMENT_HIGHLIGHTING);
    }

    /**
     * Establish setting for whether highlighting is to be applied to
     * Data Segment window.
     *
     * @param value True if highlighting is to be applied, false otherwise.
     * @deprecated Use <code>setBooleanSetting(int id, boolean value)</code> with the appropriate boolean setting ID
     * (e.g. <code>Settings.DATA_SEGMENT_HIGHLIGHTING</code>)
     */
    @Deprecated
    public void setDataSegmentHighlighting(boolean value) {
        newService.setSetting(Setting.Booleans.DATA_SEGMENT_HIGHLIGHTING, value);
    }

    /**
     * Setting concerning whether or not highlighting is applied to Registers,
     * Coprocessor0, and Coprocessor1 windows.
     *
     * @return true if highlighting is to be applied, false otherwise.
     * @deprecated Use <code>getBooleanSetting(int id)</code> with the appropriate boolean setting ID
     * (e.g. <code>Settings.REGISTERS_HIGHLIGHTING</code>)
     */
    @Deprecated
    public boolean getRegistersHighlighting() {
        return newService.getSetting(Setting.Booleans.REGISTERS_HIGHLIGHTING);
    }

    /**
     * Establish setting for whether highlighting is to be applied to
     * Registers, Coprocessor0 and Coprocessor1 windows.
     *
     * @param value True if highlighting is to be applied, false otherwise.
     * @deprecated Use <code>setBooleanSetting(int id, boolean value)</code> with the appropriate boolean setting ID
     * (e.g. <code>Settings.REGISTERS_HIGHLIGHTING</code>)
     */
    @Deprecated
    public void setRegistersHighlighting(boolean value) {
        newService.setSetting(Setting.Booleans.REGISTERS_HIGHLIGHTING, value);
    }

    /**
     * Setting concerning whether or not assembler will automatically initialize
     * the program counter to address of statement labeled 'main' if defined.
     *
     * @return true if it initializes to 'main', false otherwise.
     * @deprecated Use <code>getBooleanSetting(int id)</code> with the appropriate boolean setting ID
     * (e.g. <code>Settings.START_AT_MAIN</code>)
     */
    @Deprecated
    public boolean getStartAtMain() {
        return newService.getSetting(Setting.Booleans.START_AT_MAIN);
    }

    /**
     * Establish setting for whether assembler will automatically initialize
     * program counter to address of statement labeled 'main' if defined.
     *
     * @param value True if PC set to address of 'main', false otherwise.
     * @deprecated Use <code>setBooleanSetting(int id, boolean value)</code> with the appropriate boolean setting ID
     * (e.g. <code>Settings.START_AT_MAIN</code>)
     */
    @Deprecated
    public void setStartAtMain(boolean value) {
        newService.setSetting(Setting.Booleans.START_AT_MAIN, value);
    }

    /**
     * Name of currently selected exception handler file.
     *
     * @return String pathname of current exception handler file, empty if none.
     */
    @Deprecated
    public String getExceptionHandler() {
        return newService.getSetting(Setting.Strings.EXCEPTION_HANDLER);
    }

    /**
     * Set name of exception handler file and write it to persistent storage.
     *
     * @param newFilename name of exception handler file
     */
    @Deprecated
    public void setExceptionHandler(String newFilename) {
        newService.setSetting(Setting.Strings.EXCEPTION_HANDLER, newFilename);
    }

    /**
     * Returns identifier of current built-in memory configuration.
     *
     * @return String identifier of current built-in memory configuration, empty if none.
     */
    @Deprecated
    public String getMemoryConfiguration() {
        return newService.getSetting(Setting.Strings.MEMORY_CONFIGURATION);
    }

    /**
     * Store the identifier of the memory configuration.
     *
     * @param config A string that identifies the current built-in memory configuration
     */
    @Deprecated
    public void setMemoryConfiguration(String config) {
        newService.setSetting(Setting.Strings.MEMORY_CONFIGURATION, config);
    }

    /**
     * Current editor font.  Retained for compatibility but replaced
     * by: getFontByPosition(Settings.EDITOR_FONT)
     *
     * @return Font object for current editor font.
     */
    @Deprecated
    public Font getEditorFont() {
        return newService.getSetting(Setting.Fonts.EDITOR_FONT);
    }

    /**
     * Set editor font to the specified Font object and write it to persistent storage.
     * This method retained for compatibility but replaced by:
     * setFontByPosition(Settings.EDITOR_FONT, font)
     *
     * @param font Font object to be used by text editor.
     */
    @Deprecated
    public void setEditorFont(Font font) {
        newService.setSetting(Setting.Fonts.EDITOR_FONT, font);
    }

    /**
     * Retrieve a Font setting
     *
     * @param fontSettingPosition constant that identifies which item
     * @return Font object for given item
     */
    @Deprecated
    public Font getFontByPosition(int fontSettingPosition) {
        final Setting<Font> fontSetting = Arrays.stream(Setting.Fonts.values())
                .filter(setting -> setting.getOldId() != null)
                .filter(setting -> setting.getOldId() == fontSettingPosition)
                .findFirst()
                .orElse(null);
        return fontSetting == null ? null : newService.getSetting(fontSetting);
    }

    /**
     * Retrieve a default Font setting
     *
     * @param fontSettingPosition constant that identifies which item
     * @return Font object for given item
     */
    @Deprecated
    public Font getDefaultFontByPosition(int fontSettingPosition) {
        final Setting<Font> fontSetting = Arrays.stream(Setting.Fonts.values())
                .filter(setting -> setting.getOldId() != null)
                .filter(setting -> setting.getOldId() == fontSettingPosition)
                .findFirst()
                .orElse(null);
        return fontSetting == null ? null : newService.getDefaultSetting(fontSetting);
    }

    /**
     * Order of text segment display columns (there are 5, numbered 0 to 4).
     *
     * @return Array of int indicating the order.  Original order is 0 1 2 3 4.
     */
    public int[] getTextColumnOrder() {
        return getTextSegmentColumnOrder(
                newService.getSetting(Setting.Strings.TEXT_COLUMN_ORDER),
                newService.getDefaultSetting(Setting.Strings.TEXT_COLUMN_ORDER)
        );
    }

    /**
     * Store the current order of Text Segment window table columns, so the ordering
     * can be preserved and restored.
     *
     * @param columnOrder An array of int indicating column order.
     */
    public void setTextColumnOrder(int[] columnOrder) {
        StringBuilder stringifiedOrder = new StringBuilder();
        for (int j : columnOrder)
            stringifiedOrder.append(j).append(" ");
        newService.setSetting(Setting.Strings.TEXT_COLUMN_ORDER, stringifiedOrder.toString());
    }

    /**
     * Retrieve the caret blink rate in milliseconds.  Blink rate of 0 means
     * do not blink.
     *
     * @return int blink rate in milliseconds
     */
    public int getCaretBlinkRate() {
        int rate;
        try {
            rate = Integer.parseInt(newService.getSetting(Setting.Strings.CARET_BLINK_RATE));
        } catch (NumberFormatException nfe) {
            rate = Integer.parseInt(newService.getDefaultSetting(Setting.Strings.CARET_BLINK_RATE));
        }
        return rate;
    }

    /**
     * Set the caret blinking rate in milliseconds.  Rate of 0 means no blinking.
     *
     * @param rate blink rate in milliseconds
     */
    public void setCaretBlinkRate(int rate) {
        newService.setSetting(Setting.Strings.CARET_BLINK_RATE, String.valueOf(rate));
    }

    /**
     * Get the tab size in characters.
     *
     * @return tab size in characters.
     */
    public int getEditorTabSize() {
        int size;
        try {
            size = Integer.parseInt(newService.getSetting(Setting.Strings.EDITOR_TAB_SIZE));
        } catch (NumberFormatException nfe) {
            size = getDefaultEditorTabSize();
        }
        return size;
    }

    /**
     * Set the tab size in characters.
     *
     * @param size tab size in characters.
     */
    public void setEditorTabSize(int size) {
        newService.setSetting(Setting.Strings.EDITOR_TAB_SIZE, String.valueOf(size));
    }

    /**
     * Get number of letters to be matched by editor's instruction guide before popup generated (if popup enabled).
     * Should be 1 or 2.  If 1, the popup will be generated after first letter typed, based on all matches; if 2,
     * the popup will be generated after second letter typed.
     *
     * @return number of letters (should be 1 or 2).
     */
    public int getEditorPopupPrefixLength() {
        int length = 2;
        try {
            length = Integer.parseInt(newService.getSetting(Setting.Strings.EDITOR_POPUP_PREFIX_LENGTH));
        } catch (NumberFormatException ignored) {
        }
        return length;
    }

    /**
     * Set number of letters to be matched by editor's instruction guide before popup generated (if popup enabled).
     * Should be 1 or 2.  If 1, the popup will be generated after first letter typed, based on all matches; if 2,
     * the popup will be generated after second letter typed.
     *
     * @param length of letters (should be 1 or 2).
     */
    public void setEditorPopupPrefixLength(int length) {
        newService.setSetting(Setting.Strings.EDITOR_POPUP_PREFIX_LENGTH, String.valueOf(length));
    }

    /**
     * Get the text editor default tab size in characters
     *
     * @return tab size in characters
     */
    public int getDefaultEditorTabSize() {
        return Integer.parseInt(newService.getDefaultSetting(Setting.Strings.EDITOR_TAB_SIZE));
    }

    /**
     * Get the saved state of the Labels Window sorting  (can sort by either
     * label or address and either ascending or descending order).
     * Default state is 0, by ascending addresses.
     *
     * @return State value 0-7, as a String.
     */
    public String getLabelSortState() {
        return newService.getSetting(Setting.Strings.LABEL_SORT_STATE);
    }

    /**
     * Store the current state of the Labels Window sorter.  There are 8 possible states
     * as described in LabelsWindow.java
     *
     * @param state The current labels window sorting state, as a String.
     */
    public void setLabelSortState(String state) {
        newService.setSetting(Setting.Strings.LABEL_SORT_STATE, state);
    }

    /**
     * Get Color object for specified settings key.
     * Returns null if key is not found or its value is not a valid color encoding.
     *
     * @param key the Setting key
     * @return corresponding Color, or null if key not found or value not valid color
     */
    public Color getColorSettingByKey(String key) {
        return Arrays.stream(Setting.Colors.values())
                .filter(setting -> setting.getKey().equals(key))
                .findFirst()
                .map(newService::getSetting)
                .orElse(null);
    }

    /**
     * Get default Color value for specified settings key.
     * Returns null if key is not found or its value is not a valid color encoding.
     *
     * @param key the Setting key
     * @return corresponding default Color, or null if key not found or value not valid color
     */
    public Color getDefaultColorSettingByKey(String key) {
        return Arrays.stream(Setting.Colors.values())
                .filter(setting -> setting.getKey().equals(key))
                .findFirst()
                .map(newService::getDefaultSetting)
                .orElse(null);
    }

    /**
     * Get Color object for specified settings name (a static constant).
     * Returns null if argument invalid or its value is not a valid color encoding.
     *
     * @param position the Setting name (see list of static constants)
     * @return corresponding Color, or null if argument invalid or value not valid color
     */
    public Color getColorSettingByPosition(int position) {
        return Arrays.stream(Setting.Colors.values())
                .filter(setting -> setting.getOldId() != null)
                .filter(setting -> setting.getOldId() == position)
                .findFirst()
                .map(newService::getSetting)
                .orElse(null);
    }

    /**
     * Get default Color object for specified settings name (a static constant).
     * Returns null if argument invalid or its value is not a valid color encoding.
     *
     * @param position the Setting name (see list of static constants)
     * @return corresponding default Color, or null if argument invalid or value not valid color
     */
    public Color getDefaultColorSettingByPosition(int position) {
        return Arrays.stream(Setting.Colors.values())
                .filter(setting -> setting.getOldId() != null)
                .filter(setting -> setting.getOldId() == position)
                .findFirst()
                .map(newService::getDefaultSetting)
                .orElse(null);
    }

    /**
     * Set value of a boolean setting given its id and the value.
     *
     * @param id    int containing the setting's identifier (constants listed above)
     * @param value boolean value to store
     * @throws IllegalArgumentException if identifier is not valid.
     */
    @Deprecated
    public void setBooleanSetting(int id, boolean value) {
        newService.setSetting(Arrays.stream(Setting.Booleans.values())
                        .filter(setting -> setting.getOldId() != null)
                        .filter(setting -> setting.getOldId() == id)
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException("Invalid boolean setting ID")),
                value);
    }

    /**
     * Temporarily establish boolean setting.  This setting will NOT be written to persisent
     * store!  Currently this is used only when running MARS from the command line
     *
     * @param id    setting identifier.  These are defined for this class as static final int.
     * @param value True to enable the setting, false otherwise.
     */
    public void setBooleanSettingNonPersistent(int id, boolean value) {
        newService.setSettingNonPersistent(Arrays.stream(Setting.Booleans.values())
                        .filter(setting -> setting.getOldId() != null)
                        .filter(setting -> setting.getOldId() == id)
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException("Invalid boolean setting ID")),
                value);
    }

    /**
     * Establish setting for whether delayed branching will be applied during
     * MIPS program execution.  This setting will NOT be written to persisent
     * store!  This method should be called only to temporarily set this
     * setting -- currently this is needed only when running MARS from the
     * command line.
     *
     * @param value True to enabled delayed branching, false otherwise.
     * @deprecated Use <code>setBooleanSettingNonPersistent(int id, boolean value)</code> with the appropriate boolean setting ID
     * (e.g. <code>Settings.DELAYED_BRANCHING_ENABLED</code>)
     */
    @Deprecated
    public void setDelayedBranchingEnabledNonPersistent(boolean value) {
        // Note: Doing assignment to array results in non-persistent
        // setting (lost when MARS terminates).  For persistent, use
        // the internalSetBooleanSetting() method instead.
        newService.setSettingNonPersistent(Setting.Booleans.DELAYED_BRANCHING_ENABLED, value);
    }

    /**
     * Store a Font setting
     *
     * @param fontSettingPosition Constant that identifies the item the font goes with
     * @param font The font to set that item to
     */
    public void setFontByPosition(int fontSettingPosition, Font font) {
        Arrays.stream(Setting.Fonts.values())
                .filter(setting -> setting.getOldId().equals(fontSettingPosition))
                .findFirst()
                .ifPresent(colorSetting -> newService.setSetting(colorSetting, font));
    }

    /**
     * Set Color object for specified settings key.  Has no effect if key is invalid.
     *
     * @param key   the Setting key
     * @param color the Color to save
     */
    @Deprecated
    public void setColorSettingByKey(String key, Color color) {
        Arrays.stream(Setting.Colors.values())
                .filter(setting -> setting.getKey().equals(key))
                .findFirst()
                .ifPresent(colorSetting -> newService.setSetting(colorSetting, color));
    }

    /**
     * Set Color object for specified settings name (a static constant). Has no effect if invalid.
     *
     * @param position the Setting name (see list of static constants)
     * @param color    the Color to save
     */
    @Deprecated
    public void setColorSettingByPosition(int position, Color color) {
        Arrays.stream(Setting.Colors.values())
                .filter(setting -> setting.getOldId() != null)
                .filter(setting -> setting.getOldId() == position)
                .findFirst()
                .ifPresent(colorSetting -> newService.setSetting(colorSetting, color));
    }

    /*
     *  Private helper to do the work of converting a string containing Text
     *  Segment window table column order into int array and returning it.
     *  If a problem occurs with the parameter string, will fall back to the
     *  default defined above.
     */
    private int[] getTextSegmentColumnOrder(String stringOfColumnIndexes, String defaultValue) {
        StringTokenizer st = new StringTokenizer(stringOfColumnIndexes);
        int[] list = new int[st.countTokens()];
        int index = 0, value;
        boolean valuesOK = true;
        while (st.hasMoreTokens()) {
            try {
                value = Integer.parseInt(st.nextToken());
            } // could be either NumberFormatException or NoSuchElementException
            catch (Exception e) {
                valuesOK = false;
                break;
            }
            list[index++] = value;
        }
        if (!valuesOK && !stringOfColumnIndexes.equals(defaultValue)) {
            return getTextSegmentColumnOrder(defaultValue, defaultValue);
        }
        return list;
    }

}