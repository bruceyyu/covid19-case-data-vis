package comp3111.covid.core;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvConstraintViolationException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CustomDateConverter extends AbstractBeanField {
    @Override
    protected Object convert(String s) throws CsvDataTypeMismatchException, CsvConstraintViolationException {
        SimpleDateFormat a = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat b = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat c = new SimpleDateFormat("yyyy-MM-dd");
        List<SimpleDateFormat> candidates = new ArrayList<>();
        candidates.add(a);
        candidates.add(b);
        candidates.add(c);

        for (SimpleDateFormat simpleDateFormat: candidates) {
            try {
                return simpleDateFormat.parse(s);

            } catch (Exception e) {

            }
        }
        throw new CsvDataTypeMismatchException("Unsupported Date Type: " + s);

    }
}