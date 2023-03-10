package app.revanced.integrations.sponsorblock;


import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Field;

import app.revanced.integrations.utils.LogHelper;

// invoke-static {p0}, Lpl/jakubweg/InjectedPlugin;->inject(Landroid/content/Context;)V
// invoke-static {}, Lpl/jakubweg/InjectedPlugin;->printSomething()V
// InlineTimeBar
public class InjectedPlugin {

    public static void printSomething() {
        LogHelper.printDebug(() -> "printSomething called");
    }

    public static void printObject(Object o, int recursive) {
        if (o == null)
            LogHelper.printDebug(() -> "Printed object is null");
        else {
            LogHelper.printDebug(() -> "Printed object ("
                        + o.getClass().getName()
                        + ") = " + o.toString());
            for (Field field : o.getClass().getDeclaredFields()) {
                if (field.getType().isPrimitive())
                    continue;
                field.setAccessible(true);
                try {
                    Object value = field.get(o);
                    try {
//                        if ("java.lang.String".equals(field.getType().getName()))
                        LogHelper.printDebug(() -> "Field: " + field.toString() + " has value " + value);
                    } catch (Exception e) {
                        LogHelper.printDebug(() -> "Field: " + field.toString() + " has value that thrown an exception in toString method");
                    }
                    if (recursive > 0 && value != null && !value.getClass().isPrimitive())
                        printObject(value, recursive - 1);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void printObject(Object o) {
        printObject(o, 0);
    }

    public static void printObject(int o) {
        printObject(Integer.valueOf(o));
    }

    public static void printObject(float o) {
        printObject(Float.valueOf(o));
    }

    public static void printObject(long o) {
        printObject(Long.valueOf(o));
    }

    public static void printStackTrace() {
        StackTraceElement[] stackTrace = (new Throwable()).getStackTrace();
        LogHelper.printDebug(() -> "Printing stack trace:");
        for (StackTraceElement element : stackTrace) {
            LogHelper.printDebug(() -> element.toString());
        }
    }

    public static void printViewStack(final View view, int spaces) {
        StringBuilder builder = new StringBuilder(spaces);
        for (int i = 0; i < spaces; i++) {
            builder.append('-');
        }
        String spacesStr = builder.toString();

        if (view == null) {
            LogHelper.printDebug(() -> spacesStr + "Null view");
            return;
        }
        if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;
            LogHelper.printDebug(() -> spacesStr + "View group: " + view);
            int childCount = group.getChildCount();
            LogHelper.printDebug(() -> spacesStr + "Children count: " + childCount);
            for (int i = 0; i < childCount; i++) {
                printViewStack(group.getChildAt(i), spaces + 1);
            }
        } else {
            LogHelper.printDebug(() -> spacesStr + "Normal view: " + view);
        }
    }
}


