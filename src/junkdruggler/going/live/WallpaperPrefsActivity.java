package junkdruggler.going.live;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.Preference.OnPreferenceChangeListener;
import android.widget.Toast;

public class WallpaperPrefsActivity extends PreferenceActivity implements OnPreferenceChangeListener {

	@Override
	protected void onCreate (Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.prefs);
		Preference p = getPreferenceScreen().findPreference("text_color");
		p.setOnPreferenceChangeListener(this);

	}

	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		if (preference.getKey().equalsIgnoreCase("text_color")) {
			try {
				String input = newValue.toString();
				if (input.length()!=7) throw new Exception ("Invalid length");
				if (!input.startsWith("#")) throw new Exception ("Invalid format");
				String r = input.substring(1,3);
				String g = input.substring(3,5);
				String b = input.substring(5,7);
				int color = Color.rgb(Integer.parseInt(r, 16), Integer.parseInt(g, 16), Integer.parseInt(b, 16));
				return true;
			} catch (Exception e) {
				Toast.makeText(this, "Invalid hex color value (example input: #ff0000).",Toast.LENGTH_SHORT).show();
				return false;
			}
		} else if (preference.getKey().equalsIgnoreCase("text_to_display")) {
			try {
				String input = newValue.toString();
				if (input.length()<1) throw new Exception ("Invalid length");
				return true;
			} catch (Exception e) {
				Toast.makeText(this, "Invalid display string.",Toast.LENGTH_SHORT).show();
				return false;
			}
		}
		return true;
	}
}
