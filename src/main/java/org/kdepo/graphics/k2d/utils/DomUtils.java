package org.kdepo.graphics.k2d.utils;

import org.w3c.dom.Element;

public class DomUtils {

    public static String resolveStringAttribute(Element element, String attributeName) {
        String value = element.getAttribute(attributeName);
        if (value.isEmpty()) {
            System.out.println("Attribute '" + attributeName + "' not found for " + element);
        }
        return value;
    }

    public static int resolveIntAttribute(Element element, String attributeName) {
        String valueStr = element.getAttribute(attributeName);
        if (valueStr.isEmpty()) {
            System.out.println("Attribute '" + attributeName + "' not found for " + element);
        }
        int value = -1;
        try {
            value = Integer.parseInt(valueStr);
        } catch (NumberFormatException e) {
            System.out.println("Attribute '" + attributeName + "' not resolved for " + valueStr);
        }
        return value;
    }

    public static double resolveDoubleAttribute(Element element, String attributeName) {
        String valueStr = element.getAttribute(attributeName);
        if (valueStr.isEmpty()) {
            System.out.println("Attribute '" + attributeName + "' not found for " + element);
        }
        double value = -1;
        try {
            value = Double.parseDouble(valueStr);
        } catch (NumberFormatException e) {
            System.out.println("Attribute '" + attributeName + "' not resolved for " + valueStr);
        }
        return value;
    }

}
