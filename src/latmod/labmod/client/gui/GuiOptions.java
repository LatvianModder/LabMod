package latmod.labmod.client.gui;
import latmod.core.gui.*;
import latmod.labmod.*;

public class GuiOptions extends GuiBasic
{
	public GuiOptions()
	{ super(Main.inst); }

	public void loadWidgets()
	{
		Button.setCentred(true, false);
		addButton(0, parent.width / 2, parent.height - 56, 200, 48, "Back");
		
		//addButton(1, width / 2, 32, 300, 48, "Something");
		addWidget(1, new Slider(this, parent.width / 2, 32, 300, 48, "Sound"));
		{
			Slider sl = new Slider(this, parent.width / 2, 96, 300, 48, null);
			sl.setBounds(GameOptions.props.rotSens, 0.1F, 3.9F);
			addWidget(2, sl);
		}
	}

	public void onWidgetEvent(int i, Widget w, String event, Object... args)
	{
		if(event == Button.PRESSED)
		{
			if(i == 0)
			{
				GameOptions.saveProps();
				Main.inst.openPrevGui();
			}
		}
		else if(event == Slider.MOVED)
		{
			if(i == 2)
			{
				GameOptions.props.rotSens = ((Slider)w).value;
				w.setText("Mouse Sens: " + (int)(GameOptions.props.rotSens * 100D) + "%");
			}
		}
	}
}
