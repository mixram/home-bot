package com.mixram.telegram.bot.services.domain;

import com.mixram.telegram.bot.utils.databinding.JsonUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author mixram on 2019-03-29.
 * @since 0.2.0.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Data3DPlastic {

    //

    @Override
    public String toString() {
        return JsonUtil.toJson(this);
    }
}
