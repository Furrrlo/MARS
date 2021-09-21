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

import mars.util.EditorFont;
import mars.venus.editors.jeditsyntax.SyntaxStyle;
import mars.venus.editors.jeditsyntax.tokenmarker.Token;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@SuppressWarnings("unused")
public interface Setting<T> {

    Integer getOldId();

    static Setting<?>[] values() {
        return Stream.<Supplier<Stream<Setting<?>>>>of(
                () -> Arrays.stream(Booleans.values()),
                () -> Arrays.stream(Strings.values()),
                () -> Arrays.stream(Fonts.values()),
                () -> Arrays.stream(Colors.values()),
                () -> Arrays.stream(SyntaxStyles.values())
        ).flatMap(Supplier::get).toArray(Setting[]::new);
    }

    enum Booleans implements Setting<Boolean> {
        /**
         * Flag to determine whether or not program being assembled is limited to
         * basic MIPS instructions and formats.
         */
        EXTENDED_ASSEMBLER_ENABLED("ExtendedAssembler", 0, true),
        /**
         * Flag to determine whether or not program being assembled is limited to
         * using register numbers instead of names. NOTE: Its default value is
         * false and the IDE provides no means to change it!
         */
        BARE_MACHINE_ENABLED("BareMachine", 1, false),
        /**
         * Flag to determine whether or not a file is immediately and automatically assembled
         * upon opening. Handy when using externa editor like mipster.
         */
        ASSEMBLE_ON_OPEN_ENABLED("AssembleOnOpen", 2, false),
        /**
         * Flag to determine whether only the current editor source file (enabled false) or
         * all files in its directory (enabled true) will be assembled when assembly is selected.
         */
        ASSEMBLE_ALL_ENABLED("AssembleAll", 3, false),
        /**
         * Default visibilty of label window (symbol table).  Default only, dynamic status
         * maintained by ExecutePane
         */
        LABEL_WINDOW_VISIBILITY("LabelWindowVisibility", 4, false),
        /**
         * Default setting for displaying addresses and values in hexidecimal in the Execute
         * pane.
         */
        DISPLAY_ADDRESSES_IN_HEX("DisplayAddressesInHex", 5, true),
        DISPLAY_VALUES_IN_HEX("DisplayValuesInHex", 6, true),
        /**
         * Flag to determine whether the currently selected exception handler source file will
         * be included in each assembly operation.
         */
        EXCEPTION_HANDLER_ENABLED("LoadExceptionHandler", 7, false),
        /**
         * Flag to determine whether or not delayed branching is in effect at MIPS execution.
         * This means we simulate the pipeline and statement FOLLOWING a successful branch
         * is executed before branch is taken. DPS 14 June 2007.
         */
        DELAYED_BRANCHING_ENABLED("DelayedBranching", 8, false),
        /**
         * Flag to determine whether or not the editor will display line numbers.
         */
        EDITOR_LINE_NUMBERS_DISPLAYED("EditorLineNumbersDisplayed", 9, true),
        /**
         * Flag to determine whether or not assembler warnings are considered errors.
         */
        WARNINGS_ARE_ERRORS("WarningsAreErrors", 10, false),
        /**
         * Flag to determine whether or not to display and use program arguments
         */
        PROGRAM_ARGUMENTS("ProgramArguments", 11, false),
        /**
         * Flag to control whether or not highlighting is applied to data segment window
         */
        DATA_SEGMENT_HIGHLIGHTING("DataSegmentHighlighting", 12, true),
        /**
         * Flag to control whether or not highlighting is applied to register windows
         */
        REGISTERS_HIGHLIGHTING("RegistersHighlighting", 13, true),
        /**
         * Flag to control whether or not assembler automatically initializes program counter to 'main's address
         */
        START_AT_MAIN("StartAtMain", 14, false),
        /**
         * Flag to control whether or not editor will highlight the line currently being edited
         */
        EDITOR_CURRENT_LINE_HIGHLIGHTING("EditorCurrentLineHighlighting", 15, true),
        /**
         * Flag to control whether or not editor will provide popup instruction guidance while typing
         */
        POPUP_INSTRUCTION_GUIDANCE("PopupInstructionGuidance", 16, true),
        /**
         * Flag to control whether or not simulator will use popup dialog for input syscalls
         */
        POPUP_SYSCALL_INPUT("PopupSyscallInput", 17, false),
        /**
         * Flag to control whether or not to use generic text editor instead of language-aware styled editor
         */
        GENERIC_TEXT_EDITOR("GenericTextEditor", 18, false),
        /**
         * Flag to control whether or not language-aware editor will use auto-indent feature
         */
        AUTO_INDENT("AutoIndent", 19, true),
        /**
         * Flag to determine whether a program can write binary code to the text or data segment and
         * execute that code.
         */
        SELF_MODIFYING_CODE_ENABLED("SelfModifyingCode", 20, false),

        LAF_SYSTEM_PREFERENCES_ENABLED("LafSystemPreferencesEnabled", true),
        LAF_ACCENT_COLOR_FOLLOWS_SYSTEM("LafAccentColorFollowsSystem", true),
        LAF_SELECTION_COLOR_FOLLOWS_SYSTEM("LafSelectionColorFollowsSystem", true),
        LAF_FONT_SIZE_FOLLOWS_SYSTEM("LafFontSizeFollowsSystem", true),
        LAF_THEME_FOLLOWS_SYSTEM("LafThemeFollowsSystem", true);

        private final String key;
        private final Integer oldId;
        final SettingImpl<Boolean> settingImpl;

        Booleans(String key, boolean defaultValue) {
            this(key, null, defaultValue);
        }

        Booleans(String key, Integer oldId, boolean defaultValue) {
            this.key = key;
            this.oldId = oldId;
            this.settingImpl = new BooleanSettingImpl(key, defaultValue);
        }

        public String getKey() {
            return key;
        }

        @Override
        public Integer getOldId() {
            return oldId;
        }
    }

    enum Strings implements Setting<String> {
        /**
         * Current specified exception handler file (a MIPS assembly source file)
         */
        EXCEPTION_HANDLER("ExceptionHandler", 0, ""),
        /**
         * Order of text segment table columns
         */
        TEXT_COLUMN_ORDER("TextColumnOrder", 1, "0 1 2 3 4"),
        /**
         * State for sorting label window display
         */
        LABEL_SORT_STATE("LabelSortState", 2, "0"),

        // STRING SETTINGS.  Each array position has associated name.
        /**
         * Identifier of current memory configuration
         */
        MEMORY_CONFIGURATION("MemoryConfiguration", 3, ""),
        /**
         * Caret blink rate in milliseconds, 0 means don't blink.
         */
        CARET_BLINK_RATE("CaretBlinkRate", 4, "500"),
        /**
         * Editor tab size in characters.
         */
        EDITOR_TAB_SIZE("EditorTabSize", 5, "8"),
        /**
         * Number of letters to be matched by editor's instruction guide before popup generated (if popup enabled)
         */
        EDITOR_POPUP_PREFIX_LENGTH("EditorPopupPrefixLength", 6, "2"),

        LAF_THEME("LafTheme", null),
        LAF_FONT_SIZE("LafFontSizePreset", "100");

        private final String key;
        private final Integer oldId;
        final SettingImpl<String> settingImpl;

        Strings(String key, String defaultValue) {
            this(key, null, defaultValue);
        }

        @Deprecated
        Strings(String key, Integer oldId, String defaultValue) {
            this.key = key;
            this.oldId = oldId;
            this.settingImpl = new StringSettingImpl(key, defaultValue);
        }

        public String getKey() {
            return key;
        }

        @Override
        public Integer getOldId() {
            return oldId;
        }
    }

    /**
     * @implNote Take care not to explicitly create a Font object, since it may trigger
     *           Swing initialization.  It shouldn't, but it throws an HeadlessException
     */
    // DPS 3-Oct-2012
    // Changed default font family from "Courier New" to "Monospaced" after receiving reports that Mac were not
    // correctly rendering the left parenthesis character in the editor or text segment display.
    // See http://www.mirthcorp.com/community/issues/browse/MIRTH-1921?page=com.atlassian.jira.plugin.system.issuetabpanels:all-tabpanel
    enum Fonts implements Setting<Font> {
        /**
         * Font for the text editor
         */
        EDITOR_FONT("EditorFont", 0, "Monospaced", "Plain", "12"),
        /**
         * Font for table even row background (text, data, register displays)
         */
        EVEN_ROW_FONT("EvenRowFont", 1, "Monospaced", "Plain", "12"),
        /**
         * Font for table odd row background (text, data, register displays)
         */
        ODD_ROW_FONT("OddRowFont", 2, "Monospaced", "Plain", "12"),
        /**
         * Font for table odd row foreground (text, data, register displays)
         */
        // TODO: there is a space here D:
        TEXTSEGMENT_HIGHLIGHT_FONT( " TextSegmentHighlightFont", 3, "Monospaced", "Plain", "12"),
        /**
         * Font for text segment delay slot highlighted background
         */
        TEXTSEGMENT_DELAYSLOT_HIGHLIGHT_FONT( "TextSegmentDelayslotHighightFont", 4, "Monospaced", "Plain", "12"),
        /**
         * Font for text segment highlighted background
         */
        DATASEGMENT_HIGHLIGHT_FONT("DataSegmentHighlightFont", 5, "Monospaced", "Plain", "12"),
        /**
         * Font for register highlighted background
         */
        REGISTER_HIGHLIGHT_FONT("RegisterHighlightFont", 6, "Monospaced", "Plain", "12");

        private final String key;
        private final Integer oldId;
        final SettingImpl<Font> settingImpl;

        Fonts(String key, String defaultFamily, String defaultStyle, String defaultSize) {
            this(key, null, defaultFamily, defaultStyle, defaultSize);
        }

        @Deprecated
        Fonts(String key, Integer oldId, String defaultFamilyIn, String defaultStyleIn, String defaultSizeIn) {
            this.key = key;
            this.oldId = oldId;

            final SettingImpl<String> familySetting, styleSetting, sizeSetting;
            this.settingImpl = new CompositeSettingImpl<>(
                    familySetting = new StringSettingImpl(key + "Family", defaultFamilyIn),
                    styleSetting = new StringSettingImpl(key + "Style", defaultStyleIn),
                    sizeSetting = new StringSettingImpl(key + "Size", defaultSizeIn)
            ) {

                @Override
                protected Font parseFromValues(SettingToValue valueProvider) {
                    return EditorFont.createFontFromStringValues(
                            valueProvider.fromSetting(familySetting),
                            valueProvider.fromSetting(styleSetting),
                            valueProvider.fromSetting(sizeSetting));
                }

                @Override
                protected Map<SettingImpl<?>, ?> writeToValues(Font value) {
                    return Map.of(
                            familySetting, value.getFamily(),
                            styleSetting, EditorFont.styleIntToStyleString(value.getStyle()),
                            sizeSetting, EditorFont.sizeIntToSizeString(value.getSize())
                    );
                }
            };
        }

        @Override
        public Integer getOldId() {
            return oldId;
        }

        public String getKey() {
            return key;
        }
    }

    /**
     * @implNote Take care not to explicitly create a Color object, since it may trigger
     *           Swing initialization (that caused problems for UC Berkeley when we
     *           created Font objects here).  It shouldn't, but then again Font shouldn't
     *           either but they said it did.  (see HeadlessException)
     */
    enum Colors implements Setting<Color> {
        /**
         * RGB color for table even row background (text, data, register displays)
         */
        EVEN_ROW_BACKGROUND("EvenRowBackground", 0, 0x00e0e0e0),
        /**
         * RGB color for table even row foreground (text, data, register displays)
         */
        EVEN_ROW_FOREGROUND("EvenRowForeground", 1, 0x0),
        /**
         * RGB color for table odd row background (text, data, register displays)
         */
        ODD_ROW_BACKGROUND("OddRowBackground", 2, 0x00ffffff),
        /**
         * RGB color for table odd row foreground (text, data, register displays)
         */
        ODD_ROW_FOREGROUND("OddRowForeground", 3, 0x0),
        /**
         * RGB color for text segment highlighted background
         */
        TEXTSEGMENT_HIGHLIGHT_BACKGROUND("TextSegmentHighlightBackground", 4, 0x00ffff99),
        /**
         * RGB color for text segment highlighted foreground
         */
        TEXTSEGMENT_HIGHLIGHT_FOREGROUND("TextSegmentHighlightForeground", 5, 0x0),
        /**
         * RGB color for text segment delay slot highlighted background
         */
        TEXTSEGMENT_DELAYSLOT_HIGHLIGHT_BACKGROUND("TextSegmentDelaySlotHighlightBackground", 6, 0x0033ff00),
        /**
         * RGB color for text segment delay slot highlighted foreground
         */
        TEXTSEGMENT_DELAYSLOT_HIGHLIGHT_FOREGROUND("TextSegmentDelaySlotHighlightForeground", 7, 0x0),
        /**
         * RGB color for text segment highlighted background
         */
        DATASEGMENT_HIGHLIGHT_BACKGROUND("DataSegmentHighlightBackground", 8, 0x0099ccff),
        /**
         * RGB color for text segment highlighted foreground
         */
        DATASEGMENT_HIGHLIGHT_FOREGROUND("DataSegmentHighlightForeground", 9, 0),
        /**
         * RGB color for register highlighted background
         */
        REGISTER_HIGHLIGHT_BACKGROUND("RegisterHighlightBackground", 10, 0x0099cc55),
        /**
         * RGB color for register highlighted foreground
         */
        REGISTER_HIGHLIGHT_FOREGROUND("RegisterHighlightForeground", 11, 0x0),

        LAF_ACCENT_COLOR("LafAccentColor", null),
        LAF_SELECTION_COLOR("LafSelectionColor", null);

        private final String key;
        private final Integer oldId;
        final SettingImpl<Color> settingImpl;

        Colors(String key, String defaultValue) {
            this(key, null, defaultValue);
        }

        Colors(String key, int defaultArgb) {
            this(key, null, ColorSettingImpl.argbToString(defaultArgb));
        }

        Colors(String key, int defaultRed, int defaultGreen, int defaultBlue) {
            this(key, null, ColorSettingImpl.rgbToString(defaultRed, defaultGreen, defaultBlue));
        }

        @Deprecated
        Colors(String key, Integer oldId, int defaultArgb) {
            this(key, oldId, ColorSettingImpl.argbToString(defaultArgb));
        }

        @Deprecated
        Colors(String key, Integer oldId, String defaultValue) {
            this.key = key;
            this.oldId = oldId;
            this.settingImpl = new ColorSettingImpl(key, defaultValue);
        }

        @Override
        public Integer getOldId() {
            return oldId;
        }

        public String getKey() {
            return key;
        }
    }

    /**
     * @implNote For syntax styles, need to initialize from SyntaxUtilities defaults.
     *           Taking care not to explicitly create a Color object, since it may trigger
     *           Swing initialization (that caused problems for UC Berkeley when we
     *           created Font objects here).  It shouldn't, but then again Font shouldn't
     *           either but they said it did.  (see HeadlessException)
     */
    enum SyntaxStyles implements Setting<SyntaxStyle> {
        NULL(Token.NULL, 0x0, false, false),
        COMMENT1(Token.COMMENT1, 0x00CC33, true, false),//(Color.black,true,false);
        COMMENT2(Token.COMMENT2, 0x990033, true, false),
        KEYWORD1(Token.KEYWORD1, 0, 0, 255, false, false),//(Color.black,false,true);
        KEYWORD2(Token.KEYWORD2, 255, 0, 255, false, false),
        KEYWORD3(Token.KEYWORD3, 255, 0, 0, false, false),//(new Color(0x009600),false,false);
        LITERAL1(Token.LITERAL1, 0x00CC33, false, false),//(new Color(0x650099),false,false);
        LITERAL2(Token.LITERAL2, 0x00CC33, false, false),//(new Color(0x650099),false,true);
        LABEL(Token.LABEL, 0x0, true, false),//(new Color(0x990033),false,true);
        OPERATOR(Token.OPERATOR, 0x0, false, true),
        INVALID(Token.INVALID, 255, 0, 0, false, false),
        MACRO_ARG(Token.MACRO_ARG, 150, 150, 0, false, false);

        public static final SyntaxStyles[] TOKEN_SYNTAX_STYLES;
        static {
            final Map<Integer, SyntaxStyles> tokenToStyle = Arrays.stream(Setting.SyntaxStyles.values())
                    .collect(Collectors.toMap(Setting.SyntaxStyles::getToken, s -> s));
            // All need to be assigned even if not used by language (no gaps in array)
            final List<Integer> missing = IntStream.range(0, Token.ID_COUNT)
                    .filter(idx -> tokenToStyle.get(idx) == null)
                    .boxed()
                    .toList();
            if(!missing.isEmpty())
                throw new AssertionError("Missing SyntaxStyles " + missing);

            final SyntaxStyles[] styles = new SyntaxStyles[Token.ID_COUNT];
            Arrays.stream(Setting.SyntaxStyles.values()).forEach(setting -> styles[setting.getToken()] = setting);
            TOKEN_SYNTAX_STYLES = styles;
        }

        private static SyntaxStyle[] DEFAULT_SYNTAX_STYLES;

        public static SyntaxStyle[] defaultSyntaxStyles() {
            if(DEFAULT_SYNTAX_STYLES != null)
                return DEFAULT_SYNTAX_STYLES;

            // Nothing missing cause the static block above us checked
            final SyntaxStyle[] styles = new SyntaxStyle[Token.ID_COUNT];
            Arrays.stream(Setting.SyntaxStyles.values()).forEach(setting -> styles[setting.getToken()] = setting.settingImpl.getDefaultValue());
            DEFAULT_SYNTAX_STYLES = styles;
            return DEFAULT_SYNTAX_STYLES;
        }

        private final int token;
        final SettingImpl<SyntaxStyle> settingImpl;

        SyntaxStyles(int token, int defaultArgb, boolean defaultItalic, boolean defaultBold) {
            this(token, ColorSettingImpl.argbToString(defaultArgb), defaultItalic, defaultBold);
        }

        SyntaxStyles(int token, int defaultRed, int defaultGreen, int defaultBlue, boolean defaultItalic, boolean defaultBold) {
            this(token, ColorSettingImpl.rgbToString(defaultRed, defaultGreen, defaultBlue), defaultItalic, defaultBold);
        }

        SyntaxStyles(int token, String defaultColor, boolean defaultItalic, boolean defaultBold) {
            this.token = token;

            final SettingImpl<Color> colorSetting;
            final SettingImpl<Boolean> italicSetting, boldSetting;
            this.settingImpl = new CompositeSettingImpl<>(
                    colorSetting = new ColorSettingImpl("SyntaxStyleColor_" + token, defaultColor),
                    boldSetting = new BooleanSettingImpl("SyntaxStyleBold_" + token, defaultBold),
                    italicSetting = new BooleanSettingImpl("SyntaxStyleItalic_" + token, defaultItalic)
            ) {
                @Override
                protected SyntaxStyle parseFromValues(SettingToValue valueProvider) {
                    return new SyntaxStyle(
                            valueProvider.fromSetting(colorSetting),
                            valueProvider.fromSetting(italicSetting),
                            valueProvider.fromSetting(boldSetting));
                }

                @Override
                protected Map<SettingImpl<?>, ?> writeToValues(SyntaxStyle value) {
                    return Map.of(
                            colorSetting, value.getColor(),
                            italicSetting, value.isItalic(),
                            boldSetting, value.isBold()
                    );
                }
            };
        }

        public int getToken() {
            return token;
        }

        @Override
        public Integer getOldId() {
            return token;
        }
    }
}
