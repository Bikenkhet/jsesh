
/*
 * Copyright ou © ou Copr. Serge Rosmorduc (2004-2020) 
 * serge.rosmorduc@cnam.fr

 * Ce logiciel est régi par la licence CeCILL-C soumise au droit français et
 * respectant les principes de diffusion des logiciels libres : "http://www.cecill.info".

 * This software is governed by the CeCILL-C license 
 * under French law : "http://www.cecill.info". 
 */
package jsesh.search.ui.specifications;

import jsesh.editor.MdCSearchQuery;

/**
 * Interface for generic search interface.
 * @author Serge Rosmorduc
 */
public interface JSearchFormModelIF {
    
    public MdCSearchQuery getQuery();
}
