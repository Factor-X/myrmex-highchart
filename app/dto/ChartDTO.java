package dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Created by florian on 7/01/15.
 */
public class ChartDTO extends DTO{

    private String type;
    private String options;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    @Override
    public String toString() {
        return "ChartDTO{" +
                "options='" + options + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
