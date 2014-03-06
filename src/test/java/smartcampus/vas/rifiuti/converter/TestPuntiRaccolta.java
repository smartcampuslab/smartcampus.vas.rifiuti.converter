/*******************************************************************************
 * Copyright 2012-2013 Trento RISE
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either   express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package smartcampus.vas.rifiuti.converter;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import eu.trentorise.smartcampus.rifiuti.SQLQueries;
import eu.trentorise.smartcampus.rifiuti.model.Aree;
import eu.trentorise.smartcampus.rifiuti.model.ProfiloUtente;
import eu.trentorise.smartcampus.rifiuti.model.PuntiRaccolta;

/**
 * @author raman
 *
 */
public class TestPuntiRaccolta {

	@Test
	public void testIsole() throws Exception {
		SQLQueries q = new SQLQueries();
		List<Aree> aree = q.getAree();
		
		ProfiloUtente pu = new ProfiloUtente();
		pu.setUtenza("utenza domestica");
		for (Aree a : aree) {
			if (a.getComune() == null || a.getComune().isEmpty()) {
				continue;
			}
			System.err.println("======================");
			System.err.println("Checking "+a.getNome());
			pu.setAree(q.getAree(a.getNome()));
			List<PuntiRaccolta> list = new SQLQueries().getPuntiDiRaccoltaPerTipoPuntoRaccolta("Isola Ecologica", pu);
			for (PuntiRaccolta pr : list) {
				Assert.assertTrue(pr.getLocalizzazione() != null && !pr.getLocalizzazione().isEmpty());
			}
		}
		
	}
}
