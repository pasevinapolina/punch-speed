package com.artioml.practice.models;

/**
 * Created by Polina P on 23.02.2017.
 */

public class AverageValuePair {
    private Result userResult;
    private Result communityResult;

    public AverageValuePair() {
    }

    public AverageValuePair(Result userResult, Result communityResult) {
        this.userResult = userResult;
        this.communityResult = communityResult;
    }

    public Result getUserResult() {
        return userResult;
    }

    public void setUserResult(Result userResult) {
        this.userResult = userResult;
    }

    public Result getCommunityResult() {
        return communityResult;
    }

    public void setCommunityResult(Result communityResult) {
        this.communityResult = communityResult;
    }


}
