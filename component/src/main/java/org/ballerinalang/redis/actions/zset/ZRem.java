/*
 * Copyright (c) 2018, WSO2 Inc. (http:www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http:www.apache.orglicensesLICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.ballerinalang.redis.actions.zset;

import org.ballerinalang.bre.Context;
import org.ballerinalang.model.types.TypeKind;
import org.ballerinalang.model.values.BInteger;
import org.ballerinalang.model.values.BMap;
import org.ballerinalang.model.values.BStringArray;
import org.ballerinalang.model.values.BValue;
import org.ballerinalang.natives.annotations.BallerinaFunction;
import org.ballerinalang.natives.annotations.Receiver;
import org.ballerinalang.redis.Constants;
import org.ballerinalang.redis.RedisDataSource;
import org.ballerinalang.redis.RedisDataSourceUtils;
import org.ballerinalang.redis.actions.AbstractRedisAction;
import org.ballerinalang.util.exceptions.BallerinaException;

/**
 * {@code {@link ZRem}} Maps with "ZREM" operation of Redis.
 *
 * @since 0.5.0
 */
@BallerinaFunction(orgName = "wso2",
                   packageName = "redis:0.0.0",
                   functionName = "zRem",
                   receiver = @Receiver(type = TypeKind.OBJECT,
                                        structType = Constants.CALLER_ACTIONS))
public class ZRem extends AbstractRedisAction {

    @Override
    public void execute(Context context) {
        BMap<String, BValue> bConnector = (BMap<String, BValue>) context.getRefArgument(0);
        RedisDataSource redisDataSource = (RedisDataSource) bConnector.getNativeData(Constants.CALLER_ACTIONS);

        String key = context.getStringArgument(0);
        BStringArray members = (BStringArray) context.getRefArgument(1);
        if (members == null) {
            throw new BallerinaException("Member array " + MUST_NOT_BE_NULL);
        }
        BInteger result = zRem(key, redisDataSource, createArrayFromBStringArray(members));
        try {
            context.setReturnValues(result);
        } catch (Throwable e) {
            context.setReturnValues(RedisDataSourceUtils.getRedisConnectorError(context, e));
        }
    }
}
