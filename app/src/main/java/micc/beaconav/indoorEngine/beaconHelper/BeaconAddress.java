package micc.beaconav.indoorEngine.beaconHelper;

import android.support.annotation.IntegerRes;

import com.estimote.sdk.Beacon;

/**
 * Created by Nagash on 20/10/2015.
 */
public class BeaconAddress {
    private Integer _minor;
    private Integer _major;

    public BeaconAddress(int minor, int major) {
        this._major = major;
        this._minor = minor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BeaconAddress that = (BeaconAddress) o;

        if (!_minor.equals(that._minor)) return false;
        return _major.equals(that._major);
    }

    @Override
    public int hashCode() {
        int result = _minor.hashCode();
        result = 31 * result + _major.hashCode();
        return result;
    }
}