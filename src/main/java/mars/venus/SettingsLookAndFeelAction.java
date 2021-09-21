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
package mars.venus;

import com.github.weisj.darklaf.settings.ThemeSettings;
import mars.Globals;
import mars.settings.Setting;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class SettingsLookAndFeelAction extends GuiAction {

    protected SettingsLookAndFeelAction(String name, Icon icon, String descrip, Integer mnemonic, KeyStroke accel, VenusUI gui) {
        super(name, icon, descrip, mnemonic, accel, gui);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ThemeSettings.showSettingsDialog(mainUI, Dialog.ModalityType.APPLICATION_MODAL);
        ThemeSettings settings = ThemeSettings.getInstance();

        Globals.getSettingsService().setSetting(Setting.Booleans.LAF_SYSTEM_PREFERENCES_ENABLED, settings.isSystemPreferencesEnabled());
        Globals.getSettingsService().setSetting(Setting.Booleans.LAF_ACCENT_COLOR_FOLLOWS_SYSTEM, settings.isAccentColorFollowsSystem());
        Globals.getSettingsService().setSetting(Setting.Booleans.LAF_SELECTION_COLOR_FOLLOWS_SYSTEM, settings.isSelectionColorFollowsSystem());
        Globals.getSettingsService().setSetting(Setting.Booleans.LAF_FONT_SIZE_FOLLOWS_SYSTEM, settings.isFontSizeFollowsSystem());
        Globals.getSettingsService().setSetting(Setting.Booleans.LAF_THEME_FOLLOWS_SYSTEM, settings.isThemeFollowsSystem());
    }
}
