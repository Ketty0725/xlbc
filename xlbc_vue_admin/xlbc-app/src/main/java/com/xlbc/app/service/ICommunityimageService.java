package com.xlbc.app.service;

import com.xlbc.app.domain.Communityimage;
import com.xlbc.common.core.page.TableDataInfo;
import com.xlbc.common.core.domain.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 笔记图片Service接口
 *
 * @author ketty
 * @date 2023-04-09
 */
public interface ICommunityimageService {

    /**
     * 查询笔记图片
     */
    Communityimage queryById(Long id);

    /**
     * 查询笔记图片列表
     */
    List<Communityimage> queryListByNoteId(Long noteId);

    /**
     * 校验并批量删除笔记图片信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
