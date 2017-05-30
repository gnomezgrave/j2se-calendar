package gnomezgrave.calendar;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.management.InvalidAttributeValueException;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

/**
 * <code>GnomezCalendar</code> is a light weight calendar module which gives your
 * J2SE program a nice look with it's appearance same as <b>Windows 7</b> Calendar gadget.<br/>
 * <br/>
 * Warning..!!!!<br/>
 * Please make sure that the parent container of <code>GnomezCalendar</code> 
 * have <a href="http://docs.oracle.com/javase/1.4.2/docs/api/java/awt/BorderLayout.html">BorderLayout</a>
 * in order to make <code>GnomezCalendar</code> working correctly.
 * <br/><br/>
 * You also should have added AbsoluteLayout to the CLASSPATH, as <code>GnomezCalendar</code> uses it to locate it's components inside.
 * <br/>
 * If you are using an IDE, simply add AbsoluteLayout as a Library.
 * 
 * @author Praneeth Nilanga Peiris
 * @version 1
 */
public class J2SE_Calendar extends javax.swing.JPanel implements Serializable {

    private Calendar c;
    private ImageIcon curDayIcon;
    private ImageIcon monthIcon;
    private ImageIcon otherDayIcon;
    private String months[] = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    private String mon[] = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    private String dayNames[] = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
    private String t;
    private String d;
    private String dt;
    private String sd;
    private JLabel days[];
    private JLabel dayLabels[];
    private int date, month, year, dayOfWeek, today, firstDay;
    private int noOfDays = 0;
    private boolean leap = false;
    private int j = 0;
    private Timer timer;
    private int thisMonth;
    private int thisYear;
    private boolean isCalendarShowing = false;
    //private

    /** Creates a new J2SE Calendar */
    public J2SE_Calendar() {
        initComponents();

        c = Calendar.getInstance();
        refreshFields();

        curDayIcon = new ImageIcon(getClass().getResource("/gnomezgrave/calendar/images/CalendarDay.png"));
        monthIcon = new ImageIcon(getClass().getResource("/gnomezgrave/calendar/images/CalendarMonth.png"));
        otherDayIcon = new ImageIcon(getClass().getResource("/gnomezgrave/calendar/images/CalendarDay2.png"));

        System.out.println("J2SE Calendar - Created By : Praneeth Nilanga Peiris - gnomez.grave@gmail.com");
        CalendarPanel.setVisible(false);
        DayPanel.setVisible(true);
        SwingUtilities.updateComponentTreeUI(this);

        days = new JLabel[]{sunday, monday, tuesday, wednesday, thursday, friday, saturday};
        dayLabels = new JLabel[]{d1, d2, d3, d4, d5, d6, d7, d8, d9, d10, d11, d12, d13, d14, d15, d16, d17, d18, d19, d20, d21, d22, d23, d24,
                    d25, d26, d27, d28, d29, d30, d31, d32, d33, d34, d35, d36, d37, d38, d39, d40, d41, d42};
        loadData();
        lOver.setVisible(false);
        c.setFirstDayOfWeek(Calendar.SUNDAY);

        timer = new Timer(2000, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                c = Calendar.getInstance();
                loadData();
            }
        });
        timer.start();

    }

    /**
     * This method will return a JLabel in the J2SE Calendar, corresponding to the <code>day</code> you enter.
     * @param day The date of the month. A value within 0-31
     * @return JLabel corresponding to the given <var>day</var>
     * @throws ArrayIndexOutOfBoundsException when requested day is not in the current month.
     */
    public JLabel getDateLabel(int day) {
        if (day >= noOfDays + firstDay) {
            throw new ArrayIndexOutOfBoundsException(day + " : Requested date is not available in this month");
        }
        return dayLabels[firstDay + day];
    }

    /**
     * This method will give you the current selected date as a <code>Date</code> object.
     * @return a Date object
     */
    public Date getSelectedDate() {
        return new Date(year - 1900, month, date);
    }

    /**
     * This method is used to set a particular Date to the J2SE Calendar.
     * @param year year as is. (ex: 2013)
     * @param month A value between 1 and 12
     * @param date A value between 1 and the number of days in the month.
     * @throws InvalidAttributeValueException If Values out of the range is given.
     */
    public void setSelectedDate(int year, int month, int date) throws InvalidAttributeValueException {
        if (month < 1 || month > 12) {
            throw new InvalidAttributeValueException(month + " : is an invalid value for a month. It should be between 1 and 12");
        }
        if (month != thisMonth || year != thisYear || date != today) {
            dayLabel.setIcon(otherDayIcon);
            lOver.setVisible(true);
            timer.stop();
        } else {
            dayLabel.setIcon(curDayIcon);
        }
        setDate(year, month - 1, date);
    }

    /**
     * Returns the JLabel holds the month view.
     * @return the JLabel holds the month view.
     */
    public JLabel getCalendarViewLabel() {
        return calendarLabel;
    }

    /**
     * Returns the JLabel[42] used to represent days of the month.
     * @return JLabel[] representing days of the month.
     */
    public JLabel[] getDayLabels() {
        return dayLabels;
    }

    /**
     * Returns the JLabel holds the single day view.
     * @return the JLabel holds the single day view.
     */
    public JLabel getDayViewLabel() {
        return dayLabel;
    }

    /**
     * Sets the background of the Month view
     * @param img ImageIcon with the corresponding background image. 
     */
    public void setMonthViewLabelImage(ImageIcon img) {
        monthIcon = img;
        calendarLabel.setIcon(img);
    }

    /**
     * Sets the background of the Day view, when the showing date is the system date.
     * @param img ImageIcon with the corresponding background image.
     */
    public void setCurrentDayViewLabelImage(ImageIcon img) {
        curDayIcon = img;
    }

    /**
     * Sets the background of the Day view, when the showing date is not the system date.
     * @param img ImageIcon with the corresponding background image.
     */
    public void setOtherDayViewLabelImage(ImageIcon img) {
        otherDayIcon = img;
    }

    /**
     * Refreshes the images.
     */
    public void refresh() {
        calendarLabel.setIcon(monthIcon);
        if (isDateChanged()) {
            dayLabel.setIcon(otherDayIcon);
            lOver.setVisible(!isCalendarShowing);
        } else {
            dayLabel.setIcon(curDayIcon);
            lOver.setVisible(false);
        }

    }

    /**
     * Shows if the date has been changed from the system date.
     * @return <code>true</code> if date is changed, <code>false</code>if not.
     */
    public boolean isDateChanged() {
        refreshFields();
        return month != thisMonth || year != thisYear || date != today;
    }

    /**
     * Sets the background Image for the Current Day View.
     * @param img ImageIcon with the corresponding background image.
     */
    public void setCurrentDateLabelImage(ImageIcon img) {
        curDayIcon = img;
    }

    /**
     * Sets the background Image for the Other Day View.
     * @param img ImageIcon with the corresponding background image.
     */
    public void setOtherDateLabelImage(ImageIcon img) {
        otherDayIcon = img;
    }

    private void setDate(int year, int month, int date) throws InvalidAttributeValueException {
        refreshFields();
        this.year = year;
        this.month = month;
        this.date = date;

        Calendar tc = c;
        if (year % 400 == 0) {
            leap = true;
        } else if (year % 4 == 0) {
            leap = true;
        } else {
            leap = false;
        }
        switch (month) {
            case 0:
            case 2:
            case 4:
            case 6:
            case 7:
            case 9:
            case 11:
                noOfDays = 31;
                break;
            case 3:
            case 5:
            case 8:
            case 10:
                noOfDays = 30;
                break;
            case 1:
                noOfDays = leap ? 29 : 28;
        }
        if (date < 1 || date > noOfDays) {
            setDate(thisYear, thisMonth, today);
            refresh();
            throw new InvalidAttributeValueException(date + " : is an invalid value for a date. It should be between 1 and " + noOfDays);
        }

        dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        tc.set(Calendar.DATE, 1);
        firstDay = tc.get(Calendar.DAY_OF_WEEK);
        firstDay--;
        int temp = 0;
        for (int i = 0; i < dayLabels.length; i++) {
            dayLabels[i].setText("");
            dayLabels[i].setOpaque(false);
            dayLabels[i].setForeground(new Color(0, 0, 0));
        }
        temp = noOfDays + firstDay;
        int a = 0;
        for (int i = firstDay; i < temp; i++) {
            a++;
            if (a == today) {
                dayLabels[i].setBackground(new Color(255, 150, 0));
                dayLabels[i].setOpaque(true);
                dayLabels[i].setForeground(new Color(255, 255, 255));
            }
            dayLabels[i].setText(a + "");
        }
        t = months[month] + " " + year;
        d = date + "";
        dt = dayNames[dayOfWeek - 1];
        sd = mon[month] + " " + (year + "").substring(2, 4);

        top.setText(t);
        top2.setText(t);
        day.setText(d);
        day2.setText(d);
        weekday.setText(dt);
        weekday2.setText(dt);

        sDay.setText(sd);
        for (int i = 0; i < days.length; i++) {
            days[i].setForeground(new Color(102, 102, 102));
        }
        days[dayOfWeek - 1].setForeground(new Color(255, 150, 0));
    }

    private void refreshFields() {
        thisYear = Calendar.getInstance().get(Calendar.YEAR);
        thisMonth = Calendar.getInstance().get(Calendar.MONTH);
        today = Calendar.getInstance().get(Calendar.DATE);
    }

    private void loadData() {
        try {
            month = c.get(Calendar.MONTH);
            date = c.get(Calendar.DATE);
            year = c.get(Calendar.YEAR);
            setDate(year, month, date);
            this.repaint();
        } catch (InvalidAttributeValueException ex) {
            Logger.getLogger(J2SE_Calendar.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void change() {
        if (month == thisMonth) {
            timer.start();
            dayLabel.setIcon(curDayIcon); // NOI18N
            lOver.setVisible(false);
            day.setForeground(new Color(255, 255, 255));
            day2.setForeground(new Color(102, 102, 102));
            top.setForeground(new Color(255, 255, 255));
            top2.setForeground(new Color(102, 102, 102));
            weekday.setForeground(new Color(255, 255, 255));
            weekday2.setForeground(new Color(102, 102, 102));
            this.repaint();
        } else {
            timer.stop();
            dayLabel.setIcon(otherDayIcon); // NOI18N
            lOver.setVisible(true);
            day.setForeground(new Color(102, 102, 102));
            day2.setForeground(new Color(255, 255, 255));
            top2.setForeground(new Color(255, 255, 255));
            top.setForeground(new Color(102, 102, 102));
            weekday2.setForeground(new Color(255, 255, 255));
            weekday.setForeground(new Color(102, 102, 102));
            this.repaint();
        }
        DayPanel.repaint();
        SwingUtilities.updateComponentTreeUI(DayPanel);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        DayPanel = new javax.swing.JPanel();
        top = new javax.swing.JLabel();
        top2 = new javax.swing.JLabel();
        day = new javax.swing.JLabel();
        day2 = new javax.swing.JLabel();
        weekday = new javax.swing.JLabel();
        weekday2 = new javax.swing.JLabel();
        lOver = new javax.swing.JLabel();
        dayLabel = new javax.swing.JLabel();
        CalendarPanel = new javax.swing.JPanel();
        sDay = new javax.swing.JLabel();
        sunday = new javax.swing.JLabel();
        monday = new javax.swing.JLabel();
        tuesday = new javax.swing.JLabel();
        wednesday = new javax.swing.JLabel();
        thursday = new javax.swing.JLabel();
        friday = new javax.swing.JLabel();
        saturday = new javax.swing.JLabel();
        d7 = new javax.swing.JLabel();
        d1 = new javax.swing.JLabel();
        d9 = new javax.swing.JLabel();
        d15 = new javax.swing.JLabel();
        d22 = new javax.swing.JLabel();
        d29 = new javax.swing.JLabel();
        d2 = new javax.swing.JLabel();
        d3 = new javax.swing.JLabel();
        d4 = new javax.swing.JLabel();
        d5 = new javax.swing.JLabel();
        d6 = new javax.swing.JLabel();
        d8 = new javax.swing.JLabel();
        d10 = new javax.swing.JLabel();
        d11 = new javax.swing.JLabel();
        d12 = new javax.swing.JLabel();
        d13 = new javax.swing.JLabel();
        d14 = new javax.swing.JLabel();
        d19 = new javax.swing.JLabel();
        d20 = new javax.swing.JLabel();
        d17 = new javax.swing.JLabel();
        d21 = new javax.swing.JLabel();
        d18 = new javax.swing.JLabel();
        d16 = new javax.swing.JLabel();
        d26 = new javax.swing.JLabel();
        d27 = new javax.swing.JLabel();
        d24 = new javax.swing.JLabel();
        d28 = new javax.swing.JLabel();
        d25 = new javax.swing.JLabel();
        d23 = new javax.swing.JLabel();
        d30 = new javax.swing.JLabel();
        d31 = new javax.swing.JLabel();
        d32 = new javax.swing.JLabel();
        d33 = new javax.swing.JLabel();
        d34 = new javax.swing.JLabel();
        d35 = new javax.swing.JLabel();
        d36 = new javax.swing.JLabel();
        d37 = new javax.swing.JLabel();
        d38 = new javax.swing.JLabel();
        d39 = new javax.swing.JLabel();
        d40 = new javax.swing.JLabel();
        d41 = new javax.swing.JLabel();
        d42 = new javax.swing.JLabel();
        bNext = new javax.swing.JButton();
        bPre = new javax.swing.JButton();
        calendarLabel = new javax.swing.JLabel();

        setOpaque(false);
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        DayPanel.setOpaque(false);
        DayPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                DayPanelMouseClicked(evt);
            }
        });
        DayPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        top.setFont(new java.awt.Font("Segoe UI", 1, 12));
        top.setForeground(new java.awt.Color(255, 255, 255));
        top.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        top.setText("July 2010");
        DayPanel.add(top, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 110, 20));

        top2.setFont(new java.awt.Font("Segoe UI", 1, 12));
        top2.setForeground(new java.awt.Color(102, 102, 102));
        top2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        top2.setText("July 2010");
        DayPanel.add(top2, new org.netbeans.lib.awtextra.AbsoluteConstraints(9, 21, 110, 20));

        day.setBackground(new java.awt.Color(255, 255, 255));
        day.setFont(new java.awt.Font("Segoe UI", 0, 70));
        day.setForeground(new java.awt.Color(255, 255, 255));
        day.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        day.setText("20");
        DayPanel.add(day, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 90, 80));

        day2.setBackground(new java.awt.Color(255, 255, 255));
        day2.setFont(new java.awt.Font("Segoe UI", 0, 70));
        day2.setForeground(new java.awt.Color(102, 102, 102));
        day2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        day2.setText("20");
        DayPanel.add(day2, new org.netbeans.lib.awtextra.AbsoluteConstraints(19, 31, 90, 80));

        weekday.setFont(new java.awt.Font("Segoe UI", 1, 12));
        weekday.setForeground(new java.awt.Color(255, 255, 255));
        weekday.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        weekday.setText("Tuesday");
        DayPanel.add(weekday, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, 110, 20));

        weekday2.setBackground(new java.awt.Color(102, 102, 102));
        weekday2.setFont(new java.awt.Font("Segoe UI", 1, 12));
        weekday2.setForeground(new java.awt.Color(102, 102, 102));
        weekday2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        weekday2.setText("Tuesday");
        DayPanel.add(weekday2, new org.netbeans.lib.awtextra.AbsoluteConstraints(9, 111, 110, 20));

        lOver.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gnomezgrave/calendar/images/curl.png"))); // NOI18N
        lOver.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lOverMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lOverMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lOverMouseExited(evt);
            }
        });
        DayPanel.add(lOver, new org.netbeans.lib.awtextra.AbsoluteConstraints(3, 107, 30, 30));

        dayLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gnomezgrave/calendar/images/CalendarDay.png"))); // NOI18N
        DayPanel.add(dayLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 130, 140));

        add(DayPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 130, 140));

        CalendarPanel.setOpaque(false);
        CalendarPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                CalendarPanelMouseClicked(evt);
            }
        });
        CalendarPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        sDay.setFont(new java.awt.Font("Segoe UI", 0, 11));
        sDay.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        sDay.setText("Jul 10");
        CalendarPanel.add(sDay, new org.netbeans.lib.awtextra.AbsoluteConstraints(24, 20, 80, -1));

        sunday.setFont(new java.awt.Font("Segoe UI", 1, 10));
        sunday.setForeground(new java.awt.Color(102, 102, 102));
        sunday.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        sunday.setText("S");
        CalendarPanel.add(sunday, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 40, 20, -1));

        monday.setFont(new java.awt.Font("Segoe UI", 1, 10));
        monday.setForeground(new java.awt.Color(102, 102, 102));
        monday.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        monday.setText("M");
        CalendarPanel.add(monday, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 40, 30, -1));

        tuesday.setFont(new java.awt.Font("Segoe UI", 1, 10));
        tuesday.setForeground(new java.awt.Color(102, 102, 102));
        tuesday.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        tuesday.setText("T");
        CalendarPanel.add(tuesday, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 40, 20, -1));

        wednesday.setFont(new java.awt.Font("Segoe UI", 1, 10));
        wednesday.setForeground(new java.awt.Color(102, 102, 102));
        wednesday.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        wednesday.setText("W");
        CalendarPanel.add(wednesday, new org.netbeans.lib.awtextra.AbsoluteConstraints(45, 40, 40, -1));

        thursday.setFont(new java.awt.Font("Segoe UI", 1, 10));
        thursday.setForeground(new java.awt.Color(102, 102, 102));
        thursday.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        thursday.setText("T");
        CalendarPanel.add(thursday, new org.netbeans.lib.awtextra.AbsoluteConstraints(65, 40, 30, -1));

        friday.setFont(new java.awt.Font("Segoe UI", 1, 10));
        friday.setForeground(new java.awt.Color(102, 102, 102));
        friday.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        friday.setText("F");
        CalendarPanel.add(friday, new org.netbeans.lib.awtextra.AbsoluteConstraints(82, 40, 30, -1));

        saturday.setFont(new java.awt.Font("Segoe UI", 1, 10));
        saturday.setForeground(new java.awt.Color(102, 102, 102));
        saturday.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        saturday.setText("S");
        CalendarPanel.add(saturday, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 40, 30, -1));

        d7.setFont(new java.awt.Font("Segoe UI", 0, 9));
        d7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        d7.setText("0");
        CalendarPanel.add(d7, new org.netbeans.lib.awtextra.AbsoluteConstraints(105, 58, 15, -1));

        d1.setFont(new java.awt.Font("Segoe UI", 0, 9));
        d1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        d1.setText("0");
        CalendarPanel.add(d1, new org.netbeans.lib.awtextra.AbsoluteConstraints(3, 58, 15, -1));

        d9.setFont(new java.awt.Font("Segoe UI", 0, 9));
        d9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        d9.setText("0");
        CalendarPanel.add(d9, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, 15, -1));

        d15.setFont(new java.awt.Font("Segoe UI", 0, 9));
        d15.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        d15.setText("0");
        CalendarPanel.add(d15, new org.netbeans.lib.awtextra.AbsoluteConstraints(3, 82, 15, -1));

        d22.setFont(new java.awt.Font("Segoe UI", 0, 9));
        d22.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        d22.setText("0");
        CalendarPanel.add(d22, new org.netbeans.lib.awtextra.AbsoluteConstraints(3, 93, 15, -1));

        d29.setFont(new java.awt.Font("Segoe UI", 0, 9));
        d29.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        d29.setText("0");
        CalendarPanel.add(d29, new org.netbeans.lib.awtextra.AbsoluteConstraints(3, 104, 15, -1));

        d2.setFont(new java.awt.Font("Segoe UI", 0, 9));
        d2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        d2.setText("0");
        CalendarPanel.add(d2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 58, 15, -1));

        d3.setFont(new java.awt.Font("Segoe UI", 0, 9));
        d3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        d3.setText("0");
        CalendarPanel.add(d3, new org.netbeans.lib.awtextra.AbsoluteConstraints(37, 58, 15, -1));

        d4.setFont(new java.awt.Font("Segoe UI", 0, 9));
        d4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        d4.setText("0");
        CalendarPanel.add(d4, new org.netbeans.lib.awtextra.AbsoluteConstraints(52, 58, 15, -1));

        d5.setFont(new java.awt.Font("Segoe UI", 0, 9));
        d5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        d5.setText("0");
        CalendarPanel.add(d5, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 58, 15, -1));

        d6.setFont(new java.awt.Font("Segoe UI", 0, 9));
        d6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        d6.setText("0");
        CalendarPanel.add(d6, new org.netbeans.lib.awtextra.AbsoluteConstraints(87, 58, 15, -1));

        d8.setFont(new java.awt.Font("Segoe UI", 0, 9));
        d8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        d8.setText("0");
        CalendarPanel.add(d8, new org.netbeans.lib.awtextra.AbsoluteConstraints(3, 70, 15, -1));

        d10.setFont(new java.awt.Font("Segoe UI", 0, 9));
        d10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        d10.setText("0");
        CalendarPanel.add(d10, new org.netbeans.lib.awtextra.AbsoluteConstraints(37, 70, 15, -1));

        d11.setFont(new java.awt.Font("Segoe UI", 0, 9));
        d11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        d11.setText("0");
        CalendarPanel.add(d11, new org.netbeans.lib.awtextra.AbsoluteConstraints(52, 70, 15, -1));

        d12.setFont(new java.awt.Font("Segoe UI", 0, 9));
        d12.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        d12.setText("0");
        CalendarPanel.add(d12, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 70, 15, -1));

        d13.setFont(new java.awt.Font("Segoe UI", 0, 9));
        d13.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        d13.setText("0");
        CalendarPanel.add(d13, new org.netbeans.lib.awtextra.AbsoluteConstraints(87, 70, 15, -1));

        d14.setFont(new java.awt.Font("Segoe UI", 0, 9));
        d14.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        d14.setText("0");
        CalendarPanel.add(d14, new org.netbeans.lib.awtextra.AbsoluteConstraints(105, 70, 15, -1));

        d19.setFont(new java.awt.Font("Segoe UI", 0, 9));
        d19.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        d19.setText("0");
        CalendarPanel.add(d19, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 82, 15, -1));

        d20.setFont(new java.awt.Font("Segoe UI", 0, 9));
        d20.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        d20.setText("0");
        CalendarPanel.add(d20, new org.netbeans.lib.awtextra.AbsoluteConstraints(87, 82, 15, -1));

        d17.setFont(new java.awt.Font("Segoe UI", 0, 9));
        d17.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        d17.setText("0");
        CalendarPanel.add(d17, new org.netbeans.lib.awtextra.AbsoluteConstraints(37, 82, 15, -1));

        d21.setFont(new java.awt.Font("Segoe UI", 0, 9));
        d21.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        d21.setText("0");
        CalendarPanel.add(d21, new org.netbeans.lib.awtextra.AbsoluteConstraints(105, 82, 15, -1));

        d18.setFont(new java.awt.Font("Segoe UI", 0, 9));
        d18.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        d18.setText("0");
        CalendarPanel.add(d18, new org.netbeans.lib.awtextra.AbsoluteConstraints(52, 82, 15, -1));

        d16.setFont(new java.awt.Font("Segoe UI", 0, 9));
        d16.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        d16.setText("0");
        CalendarPanel.add(d16, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 82, 15, -1));

        d26.setFont(new java.awt.Font("Segoe UI", 0, 9));
        d26.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        d26.setText("0");
        CalendarPanel.add(d26, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 93, 15, -1));

        d27.setFont(new java.awt.Font("Segoe UI", 0, 9));
        d27.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        d27.setText("0");
        CalendarPanel.add(d27, new org.netbeans.lib.awtextra.AbsoluteConstraints(87, 93, 15, -1));

        d24.setFont(new java.awt.Font("Segoe UI", 0, 9));
        d24.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        d24.setText("0");
        CalendarPanel.add(d24, new org.netbeans.lib.awtextra.AbsoluteConstraints(37, 93, 15, -1));

        d28.setFont(new java.awt.Font("Segoe UI", 0, 9));
        d28.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        d28.setText("0");
        CalendarPanel.add(d28, new org.netbeans.lib.awtextra.AbsoluteConstraints(105, 93, 15, -1));

        d25.setFont(new java.awt.Font("Segoe UI", 0, 9));
        d25.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        d25.setText("0");
        CalendarPanel.add(d25, new org.netbeans.lib.awtextra.AbsoluteConstraints(52, 93, 15, -1));

        d23.setFont(new java.awt.Font("Segoe UI", 0, 9));
        d23.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        d23.setText("0");
        CalendarPanel.add(d23, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 93, 15, -1));

        d30.setFont(new java.awt.Font("Segoe UI", 0, 9));
        d30.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        d30.setText("0");
        CalendarPanel.add(d30, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 104, 15, -1));

        d31.setFont(new java.awt.Font("Segoe UI", 0, 9));
        d31.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        d31.setText("0");
        CalendarPanel.add(d31, new org.netbeans.lib.awtextra.AbsoluteConstraints(37, 104, 15, -1));

        d32.setFont(new java.awt.Font("Segoe UI", 0, 9));
        d32.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        d32.setText("0");
        CalendarPanel.add(d32, new org.netbeans.lib.awtextra.AbsoluteConstraints(52, 104, 15, -1));

        d33.setFont(new java.awt.Font("Segoe UI", 0, 9));
        d33.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        d33.setText("0");
        CalendarPanel.add(d33, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 104, 15, -1));

        d34.setFont(new java.awt.Font("Segoe UI", 0, 9));
        d34.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        d34.setText("0");
        CalendarPanel.add(d34, new org.netbeans.lib.awtextra.AbsoluteConstraints(87, 104, 15, -1));

        d35.setFont(new java.awt.Font("Segoe UI", 0, 9));
        d35.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        d35.setText("0");
        CalendarPanel.add(d35, new org.netbeans.lib.awtextra.AbsoluteConstraints(105, 104, 15, -1));

        d36.setFont(new java.awt.Font("Segoe UI", 0, 9));
        d36.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        d36.setText("0");
        CalendarPanel.add(d36, new org.netbeans.lib.awtextra.AbsoluteConstraints(3, 116, 15, -1));

        d37.setFont(new java.awt.Font("Segoe UI", 0, 9));
        d37.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        d37.setText("0");
        CalendarPanel.add(d37, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 116, 15, -1));

        d38.setFont(new java.awt.Font("Segoe UI", 0, 9));
        d38.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        d38.setText("0");
        CalendarPanel.add(d38, new org.netbeans.lib.awtextra.AbsoluteConstraints(37, 116, 15, -1));

        d39.setFont(new java.awt.Font("Segoe UI", 0, 9));
        d39.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        d39.setText("0");
        CalendarPanel.add(d39, new org.netbeans.lib.awtextra.AbsoluteConstraints(52, 116, 15, -1));

        d40.setFont(new java.awt.Font("Segoe UI", 0, 9));
        d40.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        d40.setText("0");
        CalendarPanel.add(d40, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 116, 15, -1));

        d41.setFont(new java.awt.Font("Segoe UI", 0, 9));
        d41.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        d41.setText("0");
        CalendarPanel.add(d41, new org.netbeans.lib.awtextra.AbsoluteConstraints(87, 116, 15, -1));

        d42.setFont(new java.awt.Font("Segoe UI", 0, 9));
        d42.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        d42.setText("0");
        CalendarPanel.add(d42, new org.netbeans.lib.awtextra.AbsoluteConstraints(105, 116, 15, -1));

        bNext.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gnomezgrave/calendar/images/bNext.png"))); // NOI18N
        bNext.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/gnomezgrave/calendar/images/bNext-down.png"))); // NOI18N
        bNext.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/gnomezgrave/calendar/images/bNext-hot.png"))); // NOI18N
        bNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bNextActionPerformed(evt);
            }
        });
        CalendarPanel.add(bNext, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 18, 20, 20));

        bPre.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gnomezgrave/calendar/images/bPrev.png"))); // NOI18N
        bPre.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/gnomezgrave/calendar/images/bPrev-down.png"))); // NOI18N
        bPre.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/gnomezgrave/calendar/images/bPrev-hot.png"))); // NOI18N
        bPre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bPreActionPerformed(evt);
            }
        });
        CalendarPanel.add(bPre, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 18, 20, 20));

        calendarLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gnomezgrave/calendar/images/CalendarMonth.png"))); // NOI18N
        CalendarPanel.add(calendarLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 130, 140));

        add(CalendarPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 130, 140));
    }// </editor-fold>//GEN-END:initComponents

    private void lOverMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lOverMouseClicked
        month = thisMonth;
        change();
}//GEN-LAST:event_lOverMouseClicked

    private void lOverMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lOverMouseEntered
        lOver.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gnomezgrave/calendar/images/curl-hot.png")));
        this.repaint();
}//GEN-LAST:event_lOverMouseEntered

    private void lOverMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lOverMouseExited
        lOver.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gnomezgrave/calendar/images/curl.png")));
        this.repaint();
}//GEN-LAST:event_lOverMouseExited

    private void DayPanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_DayPanelMouseClicked
        if (evt.getClickCount() == 2) {
            DayPanel.setVisible(false);
            CalendarPanel.setVisible(true);
            isCalendarShowing = true;
            if (!isDateChanged()) {
                lOver.setVisible(false);
            }
            this.repaint();
        }
}//GEN-LAST:event_DayPanelMouseClicked

    private void CalendarPanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CalendarPanelMouseClicked
        if (evt.getClickCount() == 2) {
            DayPanel.setVisible(true);
            CalendarPanel.setVisible(false);
            isCalendarShowing = false;
            lOver.setVisible(true);
            this.repaint();
        }
    }//GEN-LAST:event_CalendarPanelMouseClicked

    private void bPreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bPreActionPerformed
        month--;
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DATE, date);
        loadData();
        change();
    }//GEN-LAST:event_bPreActionPerformed

    private void bNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bNextActionPerformed
        month++;
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DATE, date);
        loadData();
        change();
    }//GEN-LAST:event_bNextActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel CalendarPanel;
    private javax.swing.JPanel DayPanel;
    private javax.swing.JButton bNext;
    private javax.swing.JButton bPre;
    private javax.swing.JLabel calendarLabel;
    private javax.swing.JLabel d1;
    private javax.swing.JLabel d10;
    private javax.swing.JLabel d11;
    private javax.swing.JLabel d12;
    private javax.swing.JLabel d13;
    private javax.swing.JLabel d14;
    private javax.swing.JLabel d15;
    private javax.swing.JLabel d16;
    private javax.swing.JLabel d17;
    private javax.swing.JLabel d18;
    private javax.swing.JLabel d19;
    private javax.swing.JLabel d2;
    private javax.swing.JLabel d20;
    private javax.swing.JLabel d21;
    private javax.swing.JLabel d22;
    private javax.swing.JLabel d23;
    private javax.swing.JLabel d24;
    private javax.swing.JLabel d25;
    private javax.swing.JLabel d26;
    private javax.swing.JLabel d27;
    private javax.swing.JLabel d28;
    private javax.swing.JLabel d29;
    private javax.swing.JLabel d3;
    private javax.swing.JLabel d30;
    private javax.swing.JLabel d31;
    private javax.swing.JLabel d32;
    private javax.swing.JLabel d33;
    private javax.swing.JLabel d34;
    private javax.swing.JLabel d35;
    private javax.swing.JLabel d36;
    private javax.swing.JLabel d37;
    private javax.swing.JLabel d38;
    private javax.swing.JLabel d39;
    private javax.swing.JLabel d4;
    private javax.swing.JLabel d40;
    private javax.swing.JLabel d41;
    private javax.swing.JLabel d42;
    private javax.swing.JLabel d5;
    private javax.swing.JLabel d6;
    private javax.swing.JLabel d7;
    private javax.swing.JLabel d8;
    private javax.swing.JLabel d9;
    private javax.swing.JLabel day;
    private javax.swing.JLabel day2;
    private javax.swing.JLabel dayLabel;
    private javax.swing.JLabel friday;
    private javax.swing.JLabel lOver;
    private javax.swing.JLabel monday;
    private javax.swing.JLabel sDay;
    private javax.swing.JLabel saturday;
    private javax.swing.JLabel sunday;
    private javax.swing.JLabel thursday;
    private javax.swing.JLabel top;
    private javax.swing.JLabel top2;
    private javax.swing.JLabel tuesday;
    private javax.swing.JLabel wednesday;
    private javax.swing.JLabel weekday;
    private javax.swing.JLabel weekday2;
    // End of variables declaration//GEN-END:variables
}
