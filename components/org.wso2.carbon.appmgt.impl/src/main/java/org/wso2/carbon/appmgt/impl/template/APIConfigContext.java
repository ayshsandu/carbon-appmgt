/*
 *  Copyright WSO2 Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.wso2.carbon.appmgt.impl.template;

import org.apache.velocity.VelocityContext;
import org.wso2.carbon.appmgt.api.model.WebApp;
import org.wso2.carbon.appmgt.api.model.APIStatus;


/**
 * This will initialise a velocity context to used in the template
 * and populate it with api name, version and context and a reference to api
 */
public class APIConfigContext extends ConfigContext {

    private WebApp api;

    public APIConfigContext(WebApp api) {
        this.api = api;
    }

    @Override
    public void validate() throws APITemplateException {
        //see if api name ,version, context sets
        /*if(this.getAPIName(api) && api.getContext() && api.getId().getVersion()){
            return;
        }
        else{
            this.handleException("Required WebApp mapping not provided");
        }
        */
    }

    @Override
    public VelocityContext getContext() {
        VelocityContext context = new VelocityContext();
        //set the api name version and context
        //Ideally api object should contains apiName without @ sign. If we fix it in API object, it will change the
        // way we write provider name into DB as well, which need a data migration. Hence replace only the apiName by
        // removing @ sign before creating the velocity context.
        context.put("apiName", this.getAPIName(api).replace("@","-AT-"));
        context.put("apiVersion", api.getId().getVersion());
        context.put("apiContext", api.getContext());

        //the api object will be passed on to the template so it properties can be used to
        // customise how the synapse config is generated.
        context.put("apiObj", api);

        if (api.getStatus().equals(APIStatus.BLOCKED)) {
            context.put("apiIsBlocked", true);
        } else {
            context.put("apiIsBlocked", false);
        }

        return context;
    }

    public String getAPIName(WebApp api) {
        return api.getId().getProviderName() + "--" + api.getId().getApiName();
    }
}
