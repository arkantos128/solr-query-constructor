package ru.arkantos.solr.params.extractor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DefaultPropertyExtractor implements PropertyExtractor {

    @Override
    public Object extract(Object source, String path) {
        String[] pathParts = path.split("\\.");
        Object currentObject = source;
        for (var part : pathParts) {
            if (currentObject == null) return null;

            if (currentObject instanceof Map<?, ?> map) {
                currentObject = map.get(part);
            } else if (currentObject instanceof Object[] arr) {
                if (arr.length == 0) return null;
                int index = Integer.parseInt(part);
                if (arr.length <= index) return null;
                currentObject = arr[index];
            } else if (currentObject instanceof Collection<?> coll) {
                if (coll.isEmpty()) return null;
                int index = Integer.parseInt(part);
                if (coll.size() <= index) return null;
                currentObject = new ArrayList<>(coll).get(index);
            } else {
                currentObject = getObjectField(source, part);
            }
        }
        return currentObject;
    }

    private Object getObjectField(Object source, String path) {
        String name = path.substring(0, 1).toUpperCase() + path.substring(1);
        String[] methods = new String[]{"get" + name, "is" + name};

        for (String methodName : methods) {
            try {
                Method method = source.getClass().getMethod(methodName);
                return method.invoke(source);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException var9) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "", var9);
            }
        }
        return null;
    }
}
