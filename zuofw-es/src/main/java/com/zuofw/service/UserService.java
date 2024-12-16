package com.zuofw.service;

import cn.hutool.db.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zuofw.model.vo.SearchUserReqVO;
import com.zuofw.model.vo.SearchUserRspVO;
import com.zuofw.util.PageResponse;

import java.util.List;

public interface UserService {

    /**
     * 搜索用户
     * @param searchUserReqVO
     * @return
     */
    PageResponse<SearchUserRspVO> searchUser(SearchUserReqVO searchUserReqVO);
}