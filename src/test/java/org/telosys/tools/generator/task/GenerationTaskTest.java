package org.telosys.tools.generator.task;

import static org.junit.Assert.assertEquals;

import java.util.LinkedList;
import java.util.List;

import junit.env.telosys.tools.generator.TestsProject;

import org.junit.Test;
import org.telosys.tools.commons.ConsoleLogger;
import org.telosys.tools.commons.TelosysToolsException;
import org.telosys.tools.commons.TelosysToolsLogger;
import org.telosys.tools.commons.cfg.TelosysToolsCfg;
import org.telosys.tools.generator.target.TargetDefinition;
import org.telosys.tools.generic.model.Model;

public class GenerationTaskTest {

	private GenerationTaskResult launchGenerationTask(List<String> selectedEntities, 
			String bundleName, List<TargetDefinition> selectedTargets ) throws TelosysToolsException, Exception {
		
		TelosysProject telosysProject = TestsProject.initProjectEnv("myproject", bundleName) ;
		
		//---------- Required files loading
		System.out.println("loading TelosysToolsCfg...");
		TelosysToolsCfg telosysToolsCfg = telosysProject.loadTelosysToolsCfg();
		
		System.out.println("loading model from 'dbrep' file : " + TestsProject.REPO_FILENAME );
		Model model = telosysProject.loadModelFromDbRep(TestsProject.REPO_FILENAME);

		List<TargetDefinition> resourcesTargets = null;
		
		TelosysToolsLogger logger = new ConsoleLogger() ;
		
		StandardGenerationTask generationTask = new StandardGenerationTask(
				model, selectedEntities, 
				bundleName, selectedTargets, resourcesTargets, 
				telosysToolsCfg, logger);
		
		GenerationTaskResult generationTaskResult = generationTask.launch();
		System.out.println("Nb file(s) generated : " + generationTaskResult.getNumberOfFilesGenerated() );
		
		return generationTaskResult ;
	}
	
	@Test
	public void test1() throws TelosysToolsException, Exception {
		
		//--- List of entities to be generated
		List<String> selectedEntities = new LinkedList<String>() ;
		selectedEntities.add("Author");
		
		//--- List of targets
		List<TargetDefinition> selectedTargets = new LinkedList<TargetDefinition>();
		selectedTargets.add(new TargetDefinition("Entity Java Bean", "${BEANNAME}.java", "${SRC}/${ENTITY_PKG}", "java_bean.vm", ""));
		
		GenerationTaskResult generationTaskResult = launchGenerationTask(selectedEntities, TestsProject.BUNDLE_NAME, selectedTargets);

		assertEquals(1, generationTaskResult.getNumberOfFilesGenerated());
		assertEquals(0, generationTaskResult.getNumberOfResourcesCopied());
	}

	@Test
	public void test2() throws TelosysToolsException, Exception {
		
		//--- List of entities to be generated
		List<String> selectedEntities = new LinkedList<String>() ;
		selectedEntities.add("Author");
		selectedEntities.add("Badge");
		
		//--- List of targets
		List<TargetDefinition> selectedTargets = new LinkedList<TargetDefinition>();
		selectedTargets.add(new TargetDefinition("Entity Java Bean", "${BEANNAME}.java", "${SRC}/${ENTITY_PKG}", "java_bean.vm", ""));
		
		GenerationTaskResult generationTaskResult = launchGenerationTask(selectedEntities, TestsProject.BUNDLE_NAME, selectedTargets);

		assertEquals(2, generationTaskResult.getNumberOfFilesGenerated());
		assertEquals(0, generationTaskResult.getNumberOfResourcesCopied());
	}

	@Test
	public void test3() throws TelosysToolsException, Exception {
		
		//--- List of entities to be generated
		List<String> selectedEntities = new LinkedList<String>() ;
		selectedEntities.add("Author"); 
		selectedEntities.add("Badge");
		selectedEntities.add("BookOrderItem"); // 2 files : entity + PK
		
		//--- List of targets
		List<TargetDefinition> selectedTargets = new LinkedList<TargetDefinition>();
		selectedTargets.add(new TargetDefinition("Entity Java Bean", "${BEANNAME}.java", "${SRC}/${ENTITY_PKG}", "jpa_bean_with_links.vm", ""));
		
		GenerationTaskResult generationTaskResult = launchGenerationTask(selectedEntities, "unit-tests", selectedTargets);

		assertEquals(4, generationTaskResult.getNumberOfFilesGenerated());
		assertEquals(0, generationTaskResult.getNumberOfResourcesCopied());
	}

}
