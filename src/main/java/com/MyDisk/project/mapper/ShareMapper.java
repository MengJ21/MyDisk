package com.MyDisk.project.mapper;

import com.MyDisk.project.entity.Share;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;

@Mapper
public interface ShareMapper {

    int insertShare(Share share);

    int deleteShare(Integer id);

    List<Share> queryBySharedUserIdAndLike(Integer sharedUserId, Integer like);

    List<Share> queryBySharedUserId(Integer shareUserId);

    List<Share> queryByUserId(Integer userId);

    int deleteShareByUserIdAndFileId(Integer userId, Integer sharedUserId, Integer fileId);

    int updateLikeAndLikeTimeBy3Param(Integer userId, Integer sharedUserId, Integer fileId, Integer like, Date likeTime);

}
