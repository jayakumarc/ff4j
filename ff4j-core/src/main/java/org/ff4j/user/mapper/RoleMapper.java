package org.ff4j.user.mapper;

/*-
 * #%L
 * ff4j-core
 * %%
 * Copyright (C) 2013 - 2019 FF4J
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import org.ff4j.core.FF4jMapper;
import org.ff4j.user.FF4jRole;

/**
 * Specialization of the interface.
 *
 * @author Cedrick LUNVEN (@clunven)
 *
 * @param <STORE_OBJ>
 *      target driver object.
 */
public interface RoleMapper <REQ , RES> extends FF4jMapper < FF4jRole, REQ, RES > {}
