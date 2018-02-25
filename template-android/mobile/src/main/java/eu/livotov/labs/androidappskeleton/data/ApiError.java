package eu.livotov.labs.androidappskeleton.data;

import com.google.gson.annotations.SerializedName;
import retrofit2.Call;

public class ApiError
{
    @SerializedName("code")
    private int code;

    @SerializedName("description")
    private String description = null;

    private transient Throwable localException;
    private transient String remoteUrl;

    public ApiError()
    {
    }

    public ApiError(String error)
    {
        this.code = -1;
        this.description = error;
    }

    public ApiError(int code, String error)
    {
        this.code = code;
        this.description = error;
    }

    /**
     * Creates local error model on failed api call and throwable
     *
     * @param call failed api call
     * @param t    throwable
     * @return model which describes this error
     */
    public static ApiError fromException(Call call, Throwable t)
    {
        ApiError model = new ApiError();

        if (t != null)
        {
            model.localException = t;
            model.description = t.getMessage();
        }

        model.code = 500;
        model.remoteUrl = call != null ? call.request().url().toString() : null;

        return model;
    }

    /**
     * mybox error code
     **/

    public int getCode()
    {
        return code;
    }

    public void setCode(int code)
    {
        this.code = code;
    }

    /**
     * error message description
     **/

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    @Override
    public int hashCode()
    {
        int result = code;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }

        ApiError that = (ApiError) o;

        if (code != that.code)
        {
            return false;
        }
        return description != null ? description.equals(that.description) : that.description == null;

    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("class ErrorModel {\n");

        sb.append("  code: ").append(code).append("\n");
        sb.append("  description: ").append(description).append("\n");
        sb.append("}\n");
        return sb.toString();
    }

    public Throwable getLocalException()
    {
        return localException;
    }
}
