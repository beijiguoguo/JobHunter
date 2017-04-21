package com.cszjo.jobhunter.service.impl;

import com.cszjo.jobhunter.model.ClawerTask;
import com.cszjo.jobhunter.model.JobInfo;
import com.cszjo.jobhunter.model.RedisJobInfo;
import com.cszjo.jobhunter.service.JobsService;
import com.cszjo.jobhunter.utils.JedisUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Han on 2017/4/6.
 */
@Service
public class JobsServiceImpl implements JobsService {

    private final Logger LOGGER = LoggerFactory.getLogger(JobsServiceImpl.class);

    @Autowired
    private JedisUtils jedisUtils;

    @Override
    public int insertJobs(List<JobInfo> jobs, ClawerTask task) {

        if (jobs == null || task == null) {
            LOGGER.error("insert jobs has error, jobs = {}, task = {}", jobs, task);
            return 0;
        }

        String key = RedisJobInfo.getRedisJobInfoName(task.getId());

        for (JobInfo jobInfo : jobs) {

            String jobInfoJson = jobInfo.toString();

            jedisUtils.listAdd(key, jobInfoJson);
            LOGGER.info("add a job info to redis, key = {}, job info = {}", key, jobInfoJson);
        }
        return jobs.size();
    }
}
