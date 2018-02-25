package eu.livotov.labs.androidappskeleton.core.api.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * (c) Livotov Labs Ltd. 2012
 * Date: 31/01/2018
 */
public class DateTimeConverter extends TypeAdapter<Date>
{

    private SimpleDateFormat shortDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat fullDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private SimpleDateFormat fullDateFormatWithTZ = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    private SimpleDateFormat fullDateFormatWithTZ2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    public DateTimeConverter()
    {
        shortDateFormat.setTimeZone(TimeZone.getTimeZone("Europe/Moscow"));
        fullDateFormat.setTimeZone(TimeZone.getTimeZone("Europe/Moscow"));
        fullDateFormatWithTZ.setTimeZone(TimeZone.getTimeZone("GMT"));
    }

    @Override
    public void write(JsonWriter writer, Date value) throws IOException
    {
        if (value == null)
        {
            writer.nullValue();
            return;
        }

        writer.value(fullDateFormat.format(value));
    }

    @Override
    public Date read(JsonReader reader) throws IOException
    {
        if (reader.peek() == JsonToken.NULL)
        {
            reader.nextNull();
            return null;
        }

        final String dtString = reader.nextString();
        Date result = null;

        try
        {
            if (dtString.length() > 10)
            {
                result = fullDateFormat.parse(dtString);
            }
            else
            {
                result = shortDateFormat.parse(dtString);
            }
        }
        catch (Throwable err)
        {
        }

        if (result == null)
        {
            try
            {
                result = fullDateFormatWithTZ.parse(dtString);
            }
            catch (Throwable err)
            {
            }
        }

        if (result == null)
        {
            try
            {
                result = fullDateFormatWithTZ2.parse(dtString);
            }
            catch (Throwable err)
            {
            }
        }

        return result != null ? result : new Date(0);
    }
}
