/*
 * Copyright 2002-2013 the original author or authors.
 *
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
 */
package org.adrian.mypetclinic.validate;

import org.adrian.mypetclinic.domain.Pet;
import org.adrian.mypetclinic.util.MyPetclinicApplicationConstants;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import static org.adrian.mypetclinic.util.MyPetclinicApplicationConstants.*;

/**
 * <code>Validator</code> for <code>Pet</code> forms.
 * <p>
 * We're not using Bean Validation annotations here because it is easier to define such validation rule in Java.
 * </p>
 *
 * @author Ken Krebs
 * @author Juergen Hoeller
 * @author Marcus Adrian
 */
public class PetValidator implements Validator {

    private static final String REQUIRED = "required";

    @Override
    public void validate(Object obj, Errors errors) {
        Pet pet = (Pet) obj;
        String name = pet.getName();
        // name validate
        if (!StringUtils.hasLength(name)) {
            errors.rejectValue(PET_NAME, REQUIRED, REQUIRED);
        }

        // type validate
        if (pet.isNew() && pet.getType() == null) {
            errors.rejectValue(PET_TYPE, REQUIRED, REQUIRED);
        }

        // birth date validate
        if (pet.getBirthDate() == null) {
            errors.rejectValue(PET_BIRTH_DATE, REQUIRED, REQUIRED);
        }
    }

    /**
     * This Validator validates *just* Pet instances
     */
    @Override
    public boolean supports(Class<?> clazz) {
        return Pet.class.isAssignableFrom(clazz);
    }


}
