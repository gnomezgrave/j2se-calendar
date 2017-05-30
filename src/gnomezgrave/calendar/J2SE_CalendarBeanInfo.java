/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gnomezgrave.calendar;

import java.beans.BeanInfo;
import java.beans.SimpleBeanInfo;

/**
 *
 * @author Praneeth
 */
public class J2SE_CalendarBeanInfo extends SimpleBeanInfo {

    @Override
    public java.awt.Image getIcon(int iconType) {
        if (iconType == BeanInfo.ICON_COLOR_16x16 || iconType == BeanInfo.ICON_MONO_16x16) {
            java.awt.Image img = loadImage("/gnomezgrave/calendar/images/icon16.png");
            return img;
        }
        if (iconType == BeanInfo.ICON_COLOR_32x32 || iconType == BeanInfo.ICON_MONO_32x32) {
            java.awt.Image img = loadImage("/gnomezgrave/calendar/images/icon32.png");
            return img;
        }
        return null;
    }
}
