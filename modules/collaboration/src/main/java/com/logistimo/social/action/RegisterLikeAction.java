/*
 * Copyright © 2017 Logistimo.
 *
 * This file is part of Logistimo.
 *
 * Logistimo software is a mobile & web platform for supply chain management and remote temperature monitoring in
 * low-resource settings, made available under the terms of the GNU Affero General Public License (AGPL).
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General
 * Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License along with this program.  If not, see
 * <http://www.gnu.org/licenses/>.
 *
 * You can be released from the requirements of the license by purchasing a commercial license. To know more about
 * the commercial license, please contact us at opensource@logistimo.com
 */

package com.logistimo.social.action;

import com.google.gson.GsonBuilder;

import com.logistimo.collaboration.core.models.ContextModel;
import com.logistimo.collaboration.core.models.RegisterLikeRequestModel;
import com.logistimo.collaboration.core.models.RegisterLikeResponseModel;
import com.logistimo.security.SecureUserDetails;
import com.logistimo.services.ServiceException;
import com.logistimo.social.command.LSRegisterLikeCommand;
import com.logistimo.social.util.CollaborationDomainUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * Created by kumargaurav on 15/11/17.
 */
@Component
public class RegisterLikeAction {


  @Autowired
  @Qualifier("collabRestTemplate")
  private RestTemplate restTemplate;


  public RegisterLikeResponseModel invoke(RegisterLikeRequestModel model, SecureUserDetails sUser)
      throws ServiceException {
    //TODO: check possible values of context type
    if (model.getContextType().equalsIgnoreCase("event")) {
      ContextModel
          context =
          CollaborationDomainUtil.getEventContext(model.getContextId(), sUser.getDomainId());
      model.setContextAttribute(new GsonBuilder().create().toJson(context));
    }
    return executeCommand(model);
  }

  private RegisterLikeResponseModel executeCommand(RegisterLikeRequestModel requestModel) {
    LSRegisterLikeCommand command = new LSRegisterLikeCommand(restTemplate, requestModel);
    return command.execute();
  }

}
