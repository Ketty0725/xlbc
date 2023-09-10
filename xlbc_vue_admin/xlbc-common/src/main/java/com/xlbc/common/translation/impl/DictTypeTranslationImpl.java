package com.xlbc.common.translation.impl;

import com.xlbc.common.annotation.TranslationType;
import com.xlbc.common.constant.TransConstant;
import com.xlbc.common.core.service.DictService;
import com.xlbc.common.translation.TranslationInterface;
import com.xlbc.common.utils.StringUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 字典翻译实现
 *
 * @author Lion Li
 */
@Component
@AllArgsConstructor
@TranslationType(type = TransConstant.DICT_TYPE_TO_LABEL)
public class DictTypeTranslationImpl implements TranslationInterface<String> {

    private final DictService dictService;

    public String translation(Object key, String other) {
        if (key instanceof String && StringUtils.isNotBlank(other)) {
            return dictService.getDictLabel(other, key.toString());
        }
        return null;
    }
}
