package com.github.vincemann.smartlogger;

import com.github.vincemann.smartlogger.config.DemoConfig;
import com.github.vincemann.smartlogger.model.*;
import com.github.vincemann.smartlogger.service.*;
import com.github.vincemann.smartlogger.service.jpa.JpaLogChild2Service;
import com.github.vincemann.springrapid.core.service.exception.BadEntityException;
import com.github.vincemann.springrapid.core.slicing.RapidProfiles;
import com.github.vincemann.springrapid.coretest.slicing.RapidTestProfiles;
import com.github.vincemann.springrapid.coretest.util.TransactionalRapidTestUtil;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.LazyInitializationException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceUnitUtil;
import java.util.HashMap;
import java.util.Map;

import static com.github.vincemann.smartlogger.SmartLogger.*;


@ActiveProfiles(value = {RapidTestProfiles.TEST, RapidTestProfiles.SERVICE_TEST, RapidProfiles.SERVICE})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class SmartLoggerTest {


    static final String LOG_ENTITY_NAME = "log.entity";
    static final String LAZY_COL1_ENTITY1_NAME = "lazy Col1 Entity1";
    static final String LAZY_COL1_ENTITY2_NAME = "lazy Col1 Entity2";
    static final String COL3_ENTITY1_NAME = "first log entity3";
    static final String COL3_ENTITY1_SIDE_PROPERTY = "COl3S1DE-PROPERTY";
    static final String COL3_ENTITY2_SIDE_PROPERTY = "second S1DE-P-roperty";
    static final String COL3_SIDE_PROPERTY_KEY = "s1deProperty";
    static final String COL3_ENTITY2_NAME = "second log entity3";
    static final String LAZY_PARENT_NAME = "lazy parent";
    static final String LAZY_CHILD_NAME = "lazy child";
    static final String EAGER_CHILD_NAME = "eager child";
    static final String LAZY_COL2_ENTITY1_NAME = "lazy Col2 Entity1";
    static final String COL2_ENTITY1_SIDE_PROPERTY = "SIDE-PROPERTY";
    static final String COL2_SIDE_PROPERTY_KEY = "sideProperty";
    static final String LOG_ENTITY_LAZY_CHILDREN1_KEY = "lazyChildren1";
    static final String COL2_ENTITY1_SECOND_MAIN_PROPERTY = "second main property";
    static final String COL2_SECOND_MAIN_PROPERTY_KEY = "secondMainProperty";
    static final String LAZY_COL2_ENTITY2_NAME = "lazy Col2 Entity2";
    static final String LOG_CHILD4_1_NAME = "first log child 4";
    static final String LOG_CHILD4_2_NAME = "second log child 4";
    static final String LOG_CHILD4_3_NAME = "third log child 4";
    static final String LOG_ENTITY2_NAME = "second log-entity";


    @Autowired
    LogChildService logChildService;

    @Autowired
    JpaLogChild2Service logChild2Service;

    @Autowired
    LogChild3Service logChild3Service;

    @Autowired
    LogParentService logParentService;

    @Autowired
    LazySingleLogChildService lazySingleLogChildService;

    @Autowired
    LogEntityService logEntityService;

    @Autowired
    EagerSingleLogChildService eagerSingleLogChildService;

    @Autowired
    LogChild4Service logChild4Service;

    LogEntity logEntity;
    LogEntity logEntity2;


    LogChild lazyCol1_child1;
    LogChild lazyCol1_child2;
    LogChild2 lazyCol2_child1;
    LogChild2 lazyCol2_child2;

    LogChild4 logChild4_1;
    LogChild4 logChild4_2;
    LogChild4 logChild4_3;

    LogChild3 logChild3_1;
    LogChild3 logChild3_2;
    LogParent lazyParent;
    LazySingleLogChild lazySingleChild;
    EagerSingleLogChild eagerSingleChild;

    SmartLogger smartLogger;


    @BeforeEach
    void setUp() {

        DemoConfig.USE_SMART_LOGGER = Boolean.FALSE;
        SmartLogger.DEFAULT_CONFIG = SmartLogger.Config.builder()
                .logShortOnAlreadySeen(false)
                .callToString(false)
                .build();


        logEntity = LogEntity.builder()
                .name(LOG_ENTITY_NAME)
                .build();

        logEntity2 = LogEntity.builder()
                .name(LOG_ENTITY2_NAME)
                .build();


        lazyParent = LogParent.builder()
                .name(LAZY_PARENT_NAME)
                .build();

        lazyCol1_child1 = LogChild.builder()
                .name(LAZY_COL1_ENTITY1_NAME)
                .build();
        lazyCol1_child2 = LogChild.builder()
                .name(LAZY_COL1_ENTITY2_NAME)
                .build();

        logChild3_1 = LogChild3.builder()
                .name(COL3_ENTITY1_NAME)
                .s1deProperty(COL3_ENTITY1_SIDE_PROPERTY)
                .build();

        logChild3_2 = LogChild3.builder()
                .name(COL3_ENTITY2_NAME)
                .s1deProperty(COL3_ENTITY2_SIDE_PROPERTY)
                .build();

        lazyCol2_child1 = LogChild2.builder()
                .name(LAZY_COL2_ENTITY1_NAME)
                .sideProperty(COL2_ENTITY1_SIDE_PROPERTY)
                .secondMainProperty(COL2_ENTITY1_SECOND_MAIN_PROPERTY)
                .build();
        lazyCol2_child2 = LogChild2.builder()
                .name(LAZY_COL2_ENTITY2_NAME)
                .build();

        logChild4_1 = LogChild4.builder()
                .name(LOG_CHILD4_1_NAME)
                .build();

        logChild4_2 = LogChild4.builder()
                .name(LOG_CHILD4_2_NAME)
                .build();
        logChild4_3 = LogChild4.builder()
                .name(LOG_CHILD4_3_NAME)
                .build();

        lazySingleChild = new LazySingleLogChild(LAZY_CHILD_NAME);
        eagerSingleChild = new EagerSingleLogChild(EAGER_CHILD_NAME);
    }

    @Transactional
    @Test
    void smartLoggerCallsSmartLoggerOfDiffClass_ifIndicated() throws BadEntityException {

        DemoConfig.USE_SMART_LOGGER = Boolean.TRUE;

        LogChild2.LOGGER = SmartLogger.builder()
                .logShortForm(true)
                .build();

        smartLogger = SmartLogger.builder()
                .logShortForm(false)
                .callToString(true)
                .build();



        LogEntity savedLogEntity = logEntityService.save(logEntity);

        LogChild child11 = logChildService.save(lazyCol1_child1);
        child11.setLogEntity(savedLogEntity);

        savedLogEntity.getLazyChildren1().add(child11);

        // should also only log short form of logChild2 -> propagation
        LogChild2 logChild2 = logChild2Service.save(lazyCol2_child1);
        logChild2.setLogEntity(savedLogEntity);

        savedLogEntity.getLazyChildren2().add(logChild2);


        TestTransaction.flagForCommit();
        TestTransaction.end();

        String logResult = smartLogger.toString(savedLogEntity);
        System.err.println(logResult);

        assertContainsStringOnce(logResult, LOG_ENTITY_NAME);
        assertContainsStringOnce(logResult, LAZY_COL2_ENTITY1_NAME);
        assertContainsStringOnce(logResult, LAZY_COL1_ENTITY1_NAME);
        assertContainsStringOnce(logResult,COL2_ENTITY1_SECOND_MAIN_PROPERTY);
        assertContainsStringOnce(logResult, COL2_SECOND_MAIN_PROPERTY_KEY);
        assertContainsString(logResult,CIRCULAR_REFERENCE_STRING,2);


        // other fields ignored, not even key to see
        Assertions.assertFalse(logResult.contains(COL2_SIDE_PROPERTY_KEY));
        Assertions.assertFalse(logResult.contains(COL2_ENTITY1_SIDE_PROPERTY));

    }


    @Transactional
    @Test
    void shortLogEntity() throws BadEntityException {

        smartLogger = SmartLogger.builder()
                .logShortForm(true)
                .build();



        LogEntity savedLogEntity = logEntityService.save(logEntity);

        LogChild child11 = logChildService.save(lazyCol1_child1);
        child11.setLogEntity(savedLogEntity);

        LogChild child12 = logChildService.save(lazyCol1_child2);
        child12.setLogEntity(savedLogEntity);

        savedLogEntity.getLazyChildren1().add(child11);
        savedLogEntity.getLazyChildren1().add(child12);

        // should also only log short form of logChild2 -> propagation
        LogChild2 logChild2 = logChild2Service.save(lazyCol2_child1);
        logChild2.setLogEntity(savedLogEntity);

        savedLogEntity.getLazyChildren2().add(logChild2);


        TestTransaction.flagForCommit();
        TestTransaction.end();

        String logResult = smartLogger.toString(savedLogEntity);
        System.err.println(logResult);

        assertContainsStringOnce(logResult, LOG_ENTITY_NAME);
        assertContainsStringOnce(logResult, LAZY_COL2_ENTITY1_NAME);
        assertContainsStringOnce(logResult,COL2_ENTITY1_SECOND_MAIN_PROPERTY);
        assertContainsStringOnce(logResult, COL2_SECOND_MAIN_PROPERTY_KEY);
        assertContainsStringOnce(logResult,CIRCULAR_REFERENCE_STRING);


        // other fields ignored, not even key to see
        Assertions.assertFalse(logResult.contains(LOG_ENTITY_LAZY_CHILDREN1_KEY));
        Assertions.assertFalse(logResult.contains(LAZY_COL1_ENTITY1_NAME));
        Assertions.assertFalse(logResult.contains(LAZY_COL1_ENTITY2_NAME));
        Assertions.assertFalse(logResult.contains(COL2_SIDE_PROPERTY_KEY));
        Assertions.assertFalse(logResult.contains(COL2_ENTITY1_SIDE_PROPERTY));

    }


    @Transactional
    @Test
    void prohibitsBackrefEndlessLoop() throws BadEntityException {

//        DemoConfig.USE_LAZY_LOGGER = Boolean.TRUE;

        smartLogger =  SmartLogger.builder()
                .build();



        LogEntity savedLogEntity = logEntityService.save(logEntity);

        LogChild child11 = logChildService.save(lazyCol1_child1);
        child11.setLogEntity(savedLogEntity);

        LogChild child12 = logChildService.save(lazyCol1_child2);
        child12.setLogEntity(savedLogEntity);

        savedLogEntity.getLazyChildren1().add(child11);
        savedLogEntity.getLazyChildren1().add(child12);

        TestTransaction.flagForCommit();
        TestTransaction.end();

        String logResult = smartLogger.toString(savedLogEntity);
        System.err.println(logResult);
//        String s = savedLogEntity.toString();


        assertContainsStringOnce(logResult, LOG_ENTITY_NAME);
        assertContainsStringOnce(logResult, LAZY_COL1_ENTITY1_NAME);
        assertContainsStringOnce(logResult, LAZY_COL1_ENTITY2_NAME);
        assertContainsString(logResult, CIRCULAR_REFERENCE_STRING, 2);
        // assertContainsIdOnce(logResult, savedLogEntity.getId());
        // assertContainsIdOnce(logResult, child11.getId());
        // assertContainsIdOnce(logResult, child12.getId());
    }

    @Transactional
    @Test
    void alreadySeenEntityCanBeLoggedInShortForm() throws BadEntityException {

//        DemoConfig.USE_LAZY_LOGGER = Boolean.TRUE;

        smartLogger =  SmartLogger.builder()
                .logShortOnAlreadySeen(true)
                .build();



        LogEntity savedLogEntity = logEntityService.save(logEntity);

        // this will be logged twice
        LogChild2 logChild2 = logChild2Service.save(lazyCol2_child1);
        logChild2.setLogEntity(savedLogEntity);

        LogChild child11 = logChildService.save(lazyCol1_child1);
        child11.setLogEntity(savedLogEntity);
        child11.getLogChild2Members().add(logChild2);

        LogChild child12 = logChildService.save(lazyCol1_child2);
        child12.setLogEntity(savedLogEntity);

        savedLogEntity.getLazyChildren1().add(child11);
        savedLogEntity.getLazyChildren1().add(child12);

        savedLogEntity.getLazyChildren2().add(logChild2);

        TestTransaction.flagForCommit();
        TestTransaction.end();

        String logResult = smartLogger.toString(savedLogEntity);
        System.err.println(logResult);
//        String s = savedLogEntity.toString();


        assertContainsStringOnce(logResult, LOG_ENTITY_NAME);
        assertContainsStringOnce(logResult, LAZY_COL1_ENTITY1_NAME);
        assertContainsStringOnce(logResult, LAZY_COL1_ENTITY2_NAME);
        // important main fields logged twice
        assertContainsString(logResult, LAZY_COL2_ENTITY1_NAME,2);
        assertContainsString(logResult,COL2_ENTITY1_SECOND_MAIN_PROPERTY,2);
        assertContainsString(logResult, COL2_SECOND_MAIN_PROPERTY_KEY,2);
        // unimportant skipped when entity is logged > 1 times
        assertContainsString(logResult,COL2_ENTITY1_SIDE_PROPERTY,1);
        assertContainsString(logResult, COL2_SIDE_PROPERTY_KEY,1);
        assertContainsString(logResult, CIRCULAR_REFERENCE_STRING, 4);
//        // assertContainsIdOnce(logResult, savedLogEntity.getId());
//        // assertContainsIdOnce(logResult, child11.getId());
//        // assertContainsIdOnce(logResult, child12.getId());
    }

    @Transactional
    @Test
    void logAnnotatedCollectionFieldInShortForm() throws BadEntityException {

//        DemoConfig.USE_LAZY_LOGGER = Boolean.TRUE;

        smartLogger =  SmartLogger.builder()
                .logShortOnAlreadySeen(false)
                .callToString(false)
                .build();





        LogEntity savedLogEntity = logEntityService.save(logEntity);

        // this will be logged twice
        LogChild3 logChild31 = logChild3Service.save(logChild3_1);
        logChild31.setLogEntity(savedLogEntity);

        // this will be logged twice
        LogChild3 logChild32 = logChild3Service.save(logChild3_2);
        logChild32.setLogEntity(savedLogEntity);



        LogChild2 logChild2 = logChild2Service.save(lazyCol2_child1);
        logChild2.setLogEntity(savedLogEntity);


        savedLogEntity.getLazyChildren2().add(logChild2);
        savedLogEntity.getLogChildren3().add(logChild31);
        savedLogEntity.getLogChildren3().add(logChild32);

        TestTransaction.flagForCommit();
        TestTransaction.end();

        String logResult = smartLogger.toString(savedLogEntity);
        System.err.println(logResult);
//        String s = savedLogEntity.toString();


        assertContainsStringOnce(logResult, LOG_ENTITY_NAME);
        assertContainsStringOnce(logResult, COL3_ENTITY1_NAME);
        assertContainsStringOnce(logResult, COL3_ENTITY2_NAME);
        assertContainsString(logResult, CIRCULAR_REFERENCE_STRING, 3);

        assertContainsStringOnce(logResult, LAZY_COL2_ENTITY1_NAME);
        assertContainsStringOnce(logResult,COL2_ENTITY1_SECOND_MAIN_PROPERTY);
        assertContainsStringOnce(logResult, COL2_SECOND_MAIN_PROPERTY_KEY);
        assertContainsStringOnce(logResult, COL2_SIDE_PROPERTY_KEY);
        assertContainsStringOnce(logResult, COL2_ENTITY1_SIDE_PROPERTY);


        Assertions.assertFalse(logResult.contains(COL3_SIDE_PROPERTY_KEY));
        Assertions.assertFalse(logResult.contains(COL3_ENTITY1_SIDE_PROPERTY));
        Assertions.assertFalse(logResult.contains(COL3_ENTITY2_SIDE_PROPERTY));
    }

    @Disabled
    @Transactional
    @Test
    void logAnnotatedSingleEntityFieldInShortForm() throws BadEntityException {

//        DemoConfig.USE_LAZY_LOGGER = Boolean.TRUE;

        smartLogger =  SmartLogger.builder()
                .logShortOnAlreadySeen(false)
                .callToString(false)
                .build();





        LogEntity savedLogEntity = logEntityService.save(logEntity);

        // this will be logged twice
        LogChild3 logChild31 = logChild3Service.save(logChild3_1);
        logChild31.setLogEntity(savedLogEntity);

        // this will be logged twice
        LogChild3 logChild32 = logChild3Service.save(logChild3_2);
        logChild32.setLogEntity(savedLogEntity);



        LogChild2 logChild2 = logChild2Service.save(lazyCol2_child1);
        logChild2.setLogEntity(savedLogEntity);


        savedLogEntity.getLazyChildren2().add(logChild2);
        savedLogEntity.getLogChildren3().add(logChild31);
        savedLogEntity.getLogChildren3().add(logChild32);

        TestTransaction.flagForCommit();
        TestTransaction.end();

        String logResult = smartLogger.toString(savedLogEntity);
        System.err.println(logResult);
//        String s = savedLogEntity.toString();


        assertContainsStringOnce(logResult, LOG_ENTITY_NAME);
        assertContainsStringOnce(logResult, COL3_ENTITY1_NAME);
        assertContainsStringOnce(logResult, COL3_ENTITY2_NAME);
        assertContainsString(logResult, CIRCULAR_REFERENCE_STRING, 3);

        assertContainsStringOnce(logResult, LAZY_COL2_ENTITY1_NAME);
        assertContainsStringOnce(logResult,COL2_ENTITY1_SECOND_MAIN_PROPERTY);
        assertContainsStringOnce(logResult, COL2_SECOND_MAIN_PROPERTY_KEY);
        assertContainsStringOnce(logResult, COL2_SIDE_PROPERTY_KEY);
        assertContainsStringOnce(logResult, COL2_ENTITY1_SIDE_PROPERTY);


        Assertions.assertFalse(logResult.contains(COL3_SIDE_PROPERTY_KEY));
        Assertions.assertFalse(logResult.contains(COL3_ENTITY1_SIDE_PROPERTY));
        Assertions.assertFalse(logResult.contains(COL3_ENTITY2_SIDE_PROPERTY));
    }

    @Transactional
    @Test
    void canLogTwoSameEntities_withoutBeingDetectedAsCircularRef() throws BadEntityException {
        _canLogTwoSameEntities_withoutBeingDetectedAsCircularRef();
    }

    private void _canLogTwoSameEntities_withoutBeingDetectedAsCircularRef() throws BadEntityException {
        //        DemoConfig.USE_LAZY_LOGGER = Boolean.TRUE;

        smartLogger =  SmartLogger.builder()
                .build();

        LogEntity savedLogEntity = logEntityService.save(logEntity);

        LogChild2 logChild2 = logChild2Service.save(lazyCol2_child1);
        logChild2.setLogEntity(savedLogEntity);

        LogChild child11 = logChildService.save(lazyCol1_child1);
        child11.setLogEntity(savedLogEntity);
        child11.getLogChild2Members().add(logChild2);

        LogChild child12 = logChildService.save(lazyCol1_child2);
        child12.setLogEntity(savedLogEntity);

        savedLogEntity.getLazyChildren1().add(child11);
        savedLogEntity.getLazyChildren1().add(child12);

        savedLogEntity.getLazyChildren2().add(logChild2);

        TestTransaction.flagForCommit();
        TestTransaction.end();

        String logResult = smartLogger.toString(savedLogEntity);
        System.err.println(logResult);
//        String s = savedLogEntity.toString();


        assertContainsStringOnce(logResult, LOG_ENTITY_NAME);
        assertContainsStringOnce(logResult, LAZY_COL1_ENTITY1_NAME);
        assertContainsStringOnce(logResult, LAZY_COL1_ENTITY2_NAME);
        assertContainsString(logResult, LAZY_COL2_ENTITY1_NAME,2);
        assertContainsString(logResult,COL2_ENTITY1_SECOND_MAIN_PROPERTY,2);
        assertContainsString(logResult, COL2_SECOND_MAIN_PROPERTY_KEY,2);

        assertContainsString(logResult,COL2_ENTITY1_SIDE_PROPERTY,2);
        assertContainsString(logResult, COL2_SIDE_PROPERTY_KEY,2);
        assertContainsString(logResult, CIRCULAR_REFERENCE_STRING, 4);
//        // assertContainsIdOnce(logResult, savedLogEntity.getId());
//        // assertContainsIdOnce(logResult, child11.getId());
//        // assertContainsIdOnce(logResult, child12.getId());
    }

    @Transactional
    @Test
    void canLogTwoSameEntities_hasNoState() throws BadEntityException {

        _canLogTwoSameEntities_withoutBeingDetectedAsCircularRef();
        State state = STATE_MAP.get(Thread.currentThread().getId());
        Assertions.assertEquals(0,state.getAlreadySeen().size());
        Assertions.assertEquals(0,state.getCircularRefAlreadySeen().size());
        Assertions.assertNull(state.getRecursionDepth());
        Assertions.assertNull(state.getCallToString());
        Assertions.assertNull(state.getShortCall());
    }


    @Transactional
    @Test
    void prohibitsBackrefManyToManyEndlessLoop() throws BadEntityException {

        DemoConfig.USE_SMART_LOGGER = Boolean.TRUE;



        smartLogger =  SmartLogger.builder()
                .build();

        LogEntity savedLogEntity = logEntityService.save(logEntity);

        LogChild4 child11 = logChild4Service.save(logChild4_1);
        child11.getLogEntities().add(savedLogEntity);

        LogChild4 child12 = logChild4Service.save(logChild4_2);
        child12.getLogEntities().add(savedLogEntity);

        savedLogEntity.getLogChildren4().add(child11);
        savedLogEntity.getLogChildren4().add(child12);

        TestTransaction.flagForCommit();
        TestTransaction.end();

        String logResult = smartLogger.toString(savedLogEntity);
        System.err.println(logResult);


        assertContainsStringOnce(logResult, LOG_ENTITY_NAME);
        assertContainsStringOnce(logResult, LOG_CHILD4_1_NAME);
        assertContainsStringOnce(logResult, LOG_CHILD4_2_NAME);
        assertContainsString(logResult, CIRCULAR_REFERENCE_STRING,2);
        // assertContainsIdOnce(logResult, savedLogEntity.getId());
        // assertContainsIdOnce(logResult, child11.getId());
        // assertContainsIdOnce(logResult, child12.getId());
    }

    @Transactional
    @Test
    void prohibitsBackrefManyToManyEndlessLoop_someHaveBackRefInSetAndNonBackref() throws BadEntityException {

//        DemoConfig.USE_LAZY_LOGGER = Boolean.TRUE;


        smartLogger = SmartLogger.builder()
                .build();


        LogEntity savedLogEntity = logEntityService.save(logEntity);
        LogEntity soloLogEntity = logEntityService.save(logEntity2);

        LogChild4 child11 = logChild4Service.save(logChild4_1);
        child11.getLogEntities().add(savedLogEntity);

        LogChild4 child12 = logChild4Service.save(logChild4_2);
        child12.getLogEntities().add(savedLogEntity);

        LogChild4 child13 = logChild4Service.save(logChild4_3);
        child13.getLogEntities().add(savedLogEntity);
        child13.getLogEntities().add(soloLogEntity);

        savedLogEntity.getLogChildren4().add(child11);
        savedLogEntity.getLogChildren4().add(child12);
        savedLogEntity.getLogChildren4().add(child13);

        TestTransaction.flagForCommit();
        TestTransaction.end();

        String logResult = smartLogger.toString(savedLogEntity);
        System.err.println(logResult);


        assertContainsStringOnce(logResult, LOG_ENTITY_NAME);
        assertContainsStringOnce(logResult, LOG_ENTITY2_NAME);
        assertContainsStringOnce(logResult, LOG_CHILD4_1_NAME);
        assertContainsStringOnce(logResult, LOG_CHILD4_2_NAME);
        assertContainsStringOnce(logResult, LOG_CHILD4_3_NAME);
        assertContainsString(logResult, CIRCULAR_REFERENCE_STRING,3);
//        // assertContainsIdOnce(logResult, savedLogEntity.getId());
//        // assertContainsIdOnce(logResult, soloLogEntity.getId());
//        // assertContainsIdOnce(logResult, child11.getId());
//        // assertContainsIdOnce(logResult, child12.getId());
//        // assertContainsIdOnce(logResult, child13.getId());
    }

    @Transactional
    @Test
    void canIgnoreLazyInitException() throws BadEntityException {
        smartLogger = SmartLogger.builder()
                .onlyLogLoaded(false)
                .ignoreLazyException(true)
                .build();



        EagerSingleLogChild savedEagerSingleChild = eagerSingleLogChildService.save(eagerSingleChild);
        logEntity.setEagerChild(savedEagerSingleChild);


        LogEntity e = logEntityService.save(logEntity);
        e.getLazyChildren1().add(lazyCol1_child1);


        TestTransaction.flagForCommit();
        TestTransaction.end();

        LogEntity savedLogEntity = logEntityService.findById(e.getId()).get();


        String s = smartLogger.toString(savedLogEntity);

        System.err.println(s);

        assertContainsString(s, SmartLogger.LAZY_INIT_EXCEPTION_LIST_STRING, 3);
        Assertions.assertFalse(s.contains(LAZY_COL1_ENTITY1_NAME));

        assertContainsStringOnce(s, EAGER_CHILD_NAME);
        assertContainsStringOnce(s, LOG_ENTITY_NAME);
    }

    @Transactional
    @Test
    void canThrowLazyInitException() throws BadEntityException {
        smartLogger = SmartLogger.builder()
                .onlyLogLoaded(false)
                .ignoreLazyException(false)
                .build();



        EagerSingleLogChild savedEagerSingleChild = eagerSingleLogChildService.save(eagerSingleChild);
        logEntity.setEagerChild(savedEagerSingleChild);

        LogEntity e = logEntityService.save(logEntity);

        e.getLazyChildren1().add(lazyCol1_child1);

        TestTransaction.flagForCommit();
        TestTransaction.end();


        LogEntity savedLogEntity = logEntityService.findById(e.getId()).get();


        Assertions.assertThrows(LazyInitializationException.class, () -> smartLogger.toString(savedLogEntity));
    }

    @Transactional
    @Test
    void canIgnoreAllEntities() throws BadEntityException {
        smartLogger = SmartLogger.builder()
                .ignoreEntities(true)
                .build();

        // LazyLogger.setEntityManager(entityManager);
        // lazy Child 1
        // eager child set
        // -> nothing gets logged


        EagerSingleLogChild savedEagerSingleChild = eagerSingleLogChildService.save(eagerSingleChild);
        logEntity.setEagerChild(savedEagerSingleChild);


        LogEntity e = logEntityService.save(logEntity);
        e.getLazyChildren1().add(lazyCol1_child1);

        TestTransaction.flagForCommit();
        TestTransaction.end();

        LogEntity savedLogEntity = logEntityService.findById(e.getId()).get();


        String s = smartLogger.toString(savedLogEntity);

        System.err.println(s);

        Assertions.assertFalse(s.contains(SmartLogger.LAZY_INIT_EXCEPTION_LIST_STRING));
        Assertions.assertFalse(s.contains(SmartLogger.LAZY_INIT_EXCEPTION_STRING));
        Assertions.assertFalse(s.contains(LAZY_COL1_ENTITY1_NAME));
        Assertions.assertFalse(s.contains(EAGER_CHILD_NAME));

        assertContainsStringOnce(s, LOG_ENTITY_NAME);

    }


    @Transactional
    @Test
    void canBlacklistFields() throws BadEntityException {
        smartLogger = SmartLogger.builder()
                .visiblyIgnoredProperties(Sets.newHashSet("eagerChild", "lazyChildren1"))
                .build();

        LogChild child11 = logChildService.save(lazyCol1_child1);
        child11.setLogEntity(logEntity);
        LogChild child12 = logChildService.save(lazyCol1_child2);
        child12.setLogEntity(logEntity);

//        logEntity.setLazyChildren1(Sets.newHashSet(child11,child12));
        logEntity.getLazyChildren1().add(child11);
        logEntity.getLazyChildren1().add(child12);

        LogChild2 child21 = logChild2Service.save(lazyCol2_child1);
        child21.setLogEntity(logEntity);
        LogChild2 child22 = logChild2Service.save(lazyCol2_child2);
        child22.setLogEntity(logEntity);

        logEntity.getLazyChildren2().add(child21);
        logEntity.getLazyChildren2().add(child22);


        // only eagerCollection persists

        LogEntity saved = logEntityService.save(logEntity);
        LogEntity foundLogEntity = logEntityService.findById(saved.getId()).get();

        TestTransaction.flagForCommit();
        TestTransaction.end();

        String logResult = smartLogger.toString(foundLogEntity);

        System.err.println(logResult);

        Assertions.assertFalse(logResult.contains(LAZY_COL1_ENTITY1_NAME));
        Assertions.assertFalse(logResult.contains(LAZY_COL1_ENTITY2_NAME));
        Assertions.assertFalse(logResult.contains(EAGER_CHILD_NAME));

        assertContainsString(logResult, SmartLogger.IGNORED_STRING, 2);
        assertContainsStringOnce(logResult, LOG_ENTITY_NAME);
        assertContainsStringOnce(logResult, LAZY_COL2_ENTITY1_NAME);
        assertContainsStringOnce(logResult, LAZY_COL2_ENTITY2_NAME);
    }


    @Transactional
    @Test
    void canIgnoreUnloadedEntities_andLogLoaded_inTransactionalContext() throws BadEntityException {
        smartLogger = SmartLogger.builder()
                .build();

        // fill both lazy cols
        // lazyCol1 loaded -> gets Logged
        // lazyCol2 not loaded -> <ignored unloaded>
        // eager child -> gets logged


        EagerSingleLogChild savedEagerSingleChild = eagerSingleLogChildService.save(eagerSingleChild);
        logEntity.setEagerChild(savedEagerSingleChild);

        LogEntity logEntity = logEntityService.save(this.logEntity);

        LogChild child11 = logChildService.save(lazyCol1_child1);
        child11.setLogEntity(logEntity);
        LogChild child12 = logChildService.save(lazyCol1_child2);
        child12.setLogEntity(logEntity);

//        logEntity.setLazyChildren1(Sets.newHashSet(child11,child12));
        logEntity.getLazyChildren1().add(child11);
        logEntity.getLazyChildren1().add(child12);

        LogChild2 child21 = logChild2Service.save(lazyCol2_child1);
        child21.setLogEntity(logEntity);
        LogChild2 child22 = logChild2Service.save(lazyCol2_child2);
        child22.setLogEntity(logEntity);

//        logEntity.setLazyChildren2(Sets.newHashSet(child21,child22));
        logEntity.getLazyChildren2().add(child21);
        logEntity.getLazyChildren2().add(child22);

//        List<LogChild> resultList = entityManager.createQuery("SELECT NEW com.github.vincemann.logutil.model.LogChild(g.id, g.name,g.logEntity) FROM LogChild g").getResultList();
//        logEntity.setLazyChildren1(Sets.newHashSet(resultList));


        Long id = this.logEntity.getId();


        TestTransaction.flagForCommit();
        TestTransaction.end();

        logEntity = logEntityService.findByIdAndLoadCol1(id).get();


//        entityManager.detach(logEntity.getLazyChildren2());
        Assertions.assertTrue(isLoaded(logEntity, "lazyChildren1"));
        Assertions.assertFalse(isLoaded(logEntity, "lazyChildren2"));


        TestTransaction.start();
//        logEntity = entityManager.merge(logEntity);
        String logResult = smartLogger.toString(logEntity);

        TestTransaction.flagForCommit();
        TestTransaction.end();

        System.err.println(logResult);


        assertContainsStringOnce(logResult, LAZY_COL1_ENTITY1_NAME);
        assertContainsStringOnce(logResult, LAZY_COL1_ENTITY2_NAME);
        assertContainsStringOnce(logResult, EAGER_CHILD_NAME);
        assertContainsStringOnce(logResult, LOG_ENTITY_NAME);
        assertContainsString(logResult, IGNORED_UNLOADED_STRING,4);


        Assertions.assertFalse(logResult.contains(LAZY_COL2_ENTITY1_NAME));
        Assertions.assertFalse(logResult.contains(LAZY_COL2_ENTITY2_NAME));
    }


    @Transactional
    @Test
    void canIgnoreUnloadedEntities_andLogLoaded_inNotTransactionalContext() throws BadEntityException {
        smartLogger = SmartLogger.builder()
                .build();

        // fill both lazy cols
        // lazyCol1 loaded -> gets Logged
        // lazyCol2 not loaded -> <ignored unloaded>
        // eager child -> gets logged


        EagerSingleLogChild savedEagerSingleChild = eagerSingleLogChildService.save(eagerSingleChild);
        logEntity.setEagerChild(savedEagerSingleChild);

        LogEntity logEntity = logEntityService.save(this.logEntity);

        LogChild child11 = logChildService.save(lazyCol1_child1);
        child11.setLogEntity(logEntity);
        LogChild child12 = logChildService.save(lazyCol1_child2);
        child12.setLogEntity(logEntity);

//        logEntity.setLazyChildren1(Sets.newHashSet(child11,child12));
        logEntity.getLazyChildren1().add(child11);
        logEntity.getLazyChildren1().add(child12);

        LogChild2 child21 = logChild2Service.save(lazyCol2_child1);
        child21.setLogEntity(logEntity);
        LogChild2 child22 = logChild2Service.save(lazyCol2_child2);
        child22.setLogEntity(logEntity);

//        logEntity.setLazyChildren2(Sets.newHashSet(child21,child22));
        logEntity.getLazyChildren2().add(child21);
        logEntity.getLazyChildren2().add(child22);

//        List<LogChild> resultList = entityManager.createQuery("SELECT NEW com.github.vincemann.logutil.model.LogChild(g.id, g.name,g.logEntity) FROM LogChild g").getResultList();
//        logEntity.setLazyChildren1(Sets.newHashSet(resultList));


        Long id = this.logEntity.getId();

        TestTransaction.flagForCommit();
        TestTransaction.end();

        logEntity = logEntityService.findById(id).get();
        logEntity = logEntityService.findByIdAndLoadCol1(id).get();
//        entityManager.detach(logEntity.getLazyChildren2());
        Assertions.assertTrue(isLoaded(logEntity, "lazyChildren1"));
        Assertions.assertFalse(isLoaded(logEntity, "lazyChildren2"));

        String logResult = smartLogger.toString(logEntity);

        System.err.println(logResult);


        assertContainsStringOnce(logResult, LAZY_COL1_ENTITY1_NAME);
        assertContainsStringOnce(logResult, LAZY_COL1_ENTITY2_NAME);
        assertContainsStringOnce(logResult, EAGER_CHILD_NAME);
        assertContainsStringOnce(logResult, LOG_ENTITY_NAME);
        assertContainsString(logResult, IGNORED_UNLOADED_STRING,4);

        Assertions.assertFalse(logResult.contains(LAZY_COL2_ENTITY1_NAME));
        Assertions.assertFalse(logResult.contains(LAZY_COL2_ENTITY2_NAME));
    }

    @Transactional
    @Test
    void canIgnoreUnloadedEntities_andLogLoaded_butNotLogBlacklistedLoaded_inNotTransactionalContext() throws BadEntityException {
        smartLogger = SmartLogger.builder()
                .logLoadedBlacklist(Sets.newHashSet("lazyChildren2"))
                .build();

        // fill both lazy cols
        // lazyCol1 loaded -> gets Logged
        // lazyCol2 not loaded -> <ignored unloaded>
        // eager child -> gets logged


        EagerSingleLogChild savedEagerSingleChild = eagerSingleLogChildService.save(eagerSingleChild);
        logEntity.setEagerChild(savedEagerSingleChild);

        LogEntity logEntity = logEntityService.save(this.logEntity);

        LogChild child11 = logChildService.save(lazyCol1_child1);
        child11.setLogEntity(logEntity);
        LogChild child12 = logChildService.save(lazyCol1_child2);
        child12.setLogEntity(logEntity);

//        logEntity.setLazyChildren1(Sets.newHashSet(child11,child12));
        logEntity.getLazyChildren1().add(child11);
        logEntity.getLazyChildren1().add(child12);

        LogChild2 child21 = logChild2Service.save(lazyCol2_child1);
        child21.setLogEntity(logEntity);
        LogChild2 child22 = logChild2Service.save(lazyCol2_child2);
        child22.setLogEntity(logEntity);

//        logEntity.setLazyChildren2(Sets.newHashSet(child21,child22));
        logEntity.getLazyChildren2().add(child21);
        logEntity.getLazyChildren2().add(child22);

//        List<LogChild> resultList = entityManager.createQuery("SELECT NEW com.github.vincemann.logutil.model.LogChild(g.id, g.name,g.logEntity) FROM LogChild g").getResultList();
//        logEntity.setLazyChildren1(Sets.newHashSet(resultList));


        Long id = this.logEntity.getId();

        TestTransaction.flagForCommit();
        TestTransaction.end();

        logEntity = logEntityService.findByIdAndLoadCol1AndCol2(id).get();
//        entityManager.detach(logEntity.getLazyChildren2());
        Assertions.assertTrue(isLoaded(logEntity, "lazyChildren1"));
        Assertions.assertTrue(isLoaded(logEntity, "lazyChildren2"));


        String logResult = smartLogger.toString(logEntity);

        System.err.println(logResult);


        assertContainsStringOnce(logResult, LAZY_COL1_ENTITY1_NAME);
        assertContainsStringOnce(logResult, LAZY_COL1_ENTITY2_NAME);
        assertContainsStringOnce(logResult, EAGER_CHILD_NAME);
        assertContainsStringOnce(logResult, LOG_ENTITY_NAME);
        assertContainsStringOnce(logResult, IGNORED_LOAD_BLACKLISTED_STRING);

        Assertions.assertFalse(logResult.contains(LAZY_COL2_ENTITY1_NAME));
        Assertions.assertFalse(logResult.contains(LAZY_COL2_ENTITY2_NAME));
    }

    @Transactional
    @Test
    void canLimitAllCollectionsLogSize() throws BadEntityException {
        int maxEntitiesInCollections = 1;

        smartLogger = SmartLogger.builder()
                .maxEntitiesLoggedInCollections(maxEntitiesInCollections)
                .build();

        // fill both lazy cols
        // lazyCol1 loaded -> gets Logged
        // lazyCol2 not loaded -> <ignored unloaded>
        // eager child -> gets logged


        EagerSingleLogChild savedEagerSingleChild = eagerSingleLogChildService.save(eagerSingleChild);
        logEntity.setEagerChild(savedEagerSingleChild);

        LogEntity logEntity = logEntityService.save(this.logEntity);

        LogChild child11 = logChildService.save(lazyCol1_child1);
        child11.setLogEntity(logEntity);
        LogChild child12 = logChildService.save(lazyCol1_child2);
        child12.setLogEntity(logEntity);

//        logEntity.setLazyChildren1(Sets.newHashSet(child11,child12));
        logEntity.getLazyChildren1().add(child11);
        logEntity.getLazyChildren1().add(child12);

        LogChild2 child21 = logChild2Service.save(lazyCol2_child1);
        child21.setLogEntity(logEntity);

//        logEntity.setLazyChildren2(Sets.newHashSet(child21,child22));
        logEntity.getLazyChildren2().add(child21);

//        List<LogChild> resultList = entityManager.createQuery("SELECT NEW com.github.vincemann.logutil.model.LogChild(g.id, g.name,g.logEntity) FROM LogChild g").getResultList();
//        logEntity.setLazyChildren1(Sets.newHashSet(resultList));


        Long id = this.logEntity.getId();

        TestTransaction.flagForCommit();
        TestTransaction.end();

        logEntity = logEntityService.findByIdAndLoadCol1AndCol2(id).get();
//        entityManager.detach(logEntity.getLazyChildren2());
        Assertions.assertTrue(isLoaded(logEntity, "lazyChildren1"));
        Assertions.assertTrue(isLoaded(logEntity, "lazyChildren2"));


        String logResult = smartLogger.toString(logEntity);

        System.err.println(logResult);


        assertContainsStringOnce(logResult, LAZY_COL2_ENTITY1_NAME);
        assertContainsStringOnce(logResult, EAGER_CHILD_NAME);
        assertContainsStringOnce(logResult, LOG_ENTITY_NAME);
        assertContainsStringOnce(logResult, TOO_MANY_ENTRIES_STRING);

        Assertions.assertFalse(logResult.contains(LAZY_COL1_ENTITY1_NAME));
        Assertions.assertFalse(logResult.contains(LAZY_COL1_ENTITY2_NAME));
    }

    @Transactional
    @Test
    void canLimitSpecificCollectionsLogSize() throws BadEntityException {
        Map<String, Integer> maxEntityLimitations = new HashMap<>();
        maxEntityLimitations.put("lazyChildren1", 1);
        maxEntityLimitations.put("lazyChildren2", 1);
        maxEntityLimitations.put("eagerChildren", 3);

        smartLogger = SmartLogger.builder()
                .maxEntitiesLoggedMap(maxEntityLimitations)
                .build();

        // fill both lazy cols
        // lazyCol1 loaded -> gets Logged
        // lazyCol2 not loaded -> <ignored unloaded>
        // eager child -> gets logged


        EagerSingleLogChild savedEagerSingleChild = eagerSingleLogChildService.save(eagerSingleChild);
        logEntity.setEagerChild(savedEagerSingleChild);

        LogEntity logEntity = logEntityService.save(this.logEntity);


        LogChild child11 = logChildService.save(lazyCol1_child1);
        child11.setLogEntity(logEntity);
        LogChild child12 = logChildService.save(lazyCol1_child2);
        child12.setLogEntity(logEntity);

//        logEntity.setLazyChildren1(Sets.newHashSet(child11,child12));
        logEntity.getLazyChildren1().add(child11);
        logEntity.getLazyChildren1().add(child12);

        LogChild2 child21 = logChild2Service.save(lazyCol2_child1);
        child21.setLogEntity(logEntity);
        LogChild2 child22 = logChild2Service.save(lazyCol2_child2);
        child22.setLogEntity(logEntity);


        logEntity.getLazyChildren2().add(child21);
        logEntity.getLazyChildren2().add(child22);

        LogChild3 child31 = logChild3Service.save(logChild3_1);
        child31.setLogEntity(logEntity);
        LogChild3 child32 = logChild3Service.save(logChild3_2);
        child32.setLogEntity(logEntity);


        logEntity.getLogChildren3().add(child31);
        logEntity.getLogChildren3().add(child32);

//        List<LogChild> resultList = entityManager.createQuery("SELECT NEW com.github.vincemann.logutil.model.LogChild(g.id, g.name,g.logEntity) FROM LogChild g").getResultList();
//        logEntity.setLazyChildren1(Sets.newHashSet(resultList));


        Long id = this.logEntity.getId();

        TestTransaction.flagForCommit();
        TestTransaction.end();

        logEntity = logEntityService.findByIdAndLoadCol1AndCol2(id).get();
//        entityManager.detach(logEntity.getLazyChildren2());
        Assertions.assertTrue(isLoaded(logEntity, "lazyChildren1"));
        Assertions.assertTrue(isLoaded(logEntity, "lazyChildren2"));
        Assertions.assertTrue(isLoaded(logEntity, "eagerChildren"));


        String logResult = smartLogger.toString(logEntity);

        System.err.println(logResult);


        assertContainsStringOnce(logResult, COL3_ENTITY1_NAME);
        assertContainsStringOnce(logResult, COL3_ENTITY2_NAME);
        assertContainsStringOnce(logResult, EAGER_CHILD_NAME);
        assertContainsStringOnce(logResult, LOG_ENTITY_NAME);
        assertContainsString(logResult, TOO_MANY_ENTRIES_STRING, 2);

        Assertions.assertFalse(logResult.contains(LAZY_COL1_ENTITY1_NAME));
        Assertions.assertFalse(logResult.contains(LAZY_COL1_ENTITY2_NAME));
        Assertions.assertFalse(logResult.contains(LAZY_COL2_ENTITY2_NAME));
        Assertions.assertFalse(logResult.contains(LAZY_COL2_ENTITY2_NAME));
    }

    @Transactional
    @Test
    void canMapToId() throws BadEntityException {
        smartLogger = SmartLogger.builder()
                .idOnly(true)
                .build();

        // fill both lazy cols
        // lazyCol1 loaded -> gets Logged
        // lazyCol2 not loaded -> <ignored unloaded>
        // eager child -> gets logged


        EagerSingleLogChild savedEagerSingleChild = eagerSingleLogChildService.save(eagerSingleChild);
        logEntity.setEagerChild(savedEagerSingleChild);

        LogEntity logEntity = logEntityService.save(this.logEntity);


        LogChild child11 = logChildService.save(lazyCol1_child1);
        child11.setLogEntity(logEntity);
        LogChild child12 = logChildService.save(lazyCol1_child2);
        child12.setLogEntity(logEntity);

//        logEntity.setLazyChildren1(Sets.newHashSet(child11,child12));
        logEntity.getLazyChildren1().add(child11);
        logEntity.getLazyChildren1().add(child12);

        LogChild2 child21 = logChild2Service.save(lazyCol2_child1);
        child21.setLogEntity(logEntity);
        LogChild2 child22 = logChild2Service.save(lazyCol2_child2);
        child22.setLogEntity(logEntity);


        logEntity.getLazyChildren2().add(child21);
        logEntity.getLazyChildren2().add(child22);

//        List<LogChild> resultList = entityManager.createQuery("SELECT NEW com.github.vincemann.logutil.model.LogChild(g.id, g.name,g.logEntity) FROM LogChild g").getResultList();
//        logEntity.setLazyChildren1(Sets.newHashSet(resultList));


        Long id = this.logEntity.getId();

        TestTransaction.flagForCommit();
        TestTransaction.end();

        logEntity = logEntityService.findByIdAndLoadCol1AndCol2(id).get();

        String logResult = smartLogger.toString(logEntity);

        System.err.println(logResult);


        // assertContainsIdOnce(logResult, child11.getId());
        // assertContainsIdOnce(logResult, child12.getId());
        // assertContainsIdOnce(logResult, child21.getId());
        // assertContainsIdOnce(logResult, child22.getId());
        // assertContainsIdOnce(logResult, savedEagerSingleChild.getId());
        assertContainsStringOnce(logResult, LOG_ENTITY_NAME);

        Assertions.assertFalse(logResult.contains(LAZY_COL1_ENTITY1_NAME));
        Assertions.assertFalse(logResult.contains(LAZY_COL1_ENTITY2_NAME));
        Assertions.assertFalse(logResult.contains(LAZY_COL2_ENTITY2_NAME));
        Assertions.assertFalse(logResult.contains(LAZY_COL2_ENTITY2_NAME));
        Assertions.assertFalse(logResult.contains(EAGER_CHILD_NAME));
    }


    @Transactional
    @Test
    void canMapToId_andLimitResults() throws BadEntityException {

        Map<String, Integer> maxEntityLimitations = new HashMap<>();
        maxEntityLimitations.put("lazyChildren1", 1);
        maxEntityLimitations.put("lazyChildren2", 1);
        maxEntityLimitations.put("eagerChildren", 3);


        smartLogger = SmartLogger.builder()
                .idOnly(true)
                .maxEntitiesLoggedMap(maxEntityLimitations)
                .build();
        // LazyLogger.setEntityManager(entityManager);

        // fill both lazy cols
        // lazyCol1 loaded -> gets Logged
        // lazyCol2 not loaded -> <ignored unloaded>
        // eager child -> gets logged


        EagerSingleLogChild savedEagerSingleChild = eagerSingleLogChildService.save(eagerSingleChild);
        logEntity.setEagerChild(savedEagerSingleChild);

        LogEntity logEntity = logEntityService.save(this.logEntity);


        LogChild child11 = logChildService.save(lazyCol1_child1);
        child11.setLogEntity(logEntity);
        LogChild child12 = logChildService.save(lazyCol1_child2);
        child12.setLogEntity(logEntity);

//        logEntity.setLazyChildren1(Sets.newHashSet(child11,child12));
        logEntity.getLazyChildren1().add(child11);
        logEntity.getLazyChildren1().add(child12);

        LogChild2 child21 = logChild2Service.save(lazyCol2_child1);
        child21.setLogEntity(logEntity);
        LogChild2 child22 = logChild2Service.save(lazyCol2_child2);
        child22.setLogEntity(logEntity);


        logEntity.getLazyChildren2().add(child21);
        logEntity.getLazyChildren2().add(child22);

//        List<LogChild> resultList = entityManager.createQuery("SELECT NEW com.github.vincemann.logutil.model.LogChild(g.id, g.name,g.logEntity) FROM LogChild g").getResultList();
//        logEntity.setLazyChildren1(Sets.newHashSet(resultList));

        LogChild3 child31 = logChild3Service.save(logChild3_1);
        child31.setLogEntity(logEntity);
        LogChild3 child32 = logChild3Service.save(logChild3_2);
        child32.setLogEntity(logEntity);


        logEntity.getLogChildren3().add(child31);
        logEntity.getLogChildren3().add(child32);

        Long id = this.logEntity.getId();

        TestTransaction.flagForCommit();
        TestTransaction.end();

        logEntity = logEntityService.findByIdAndLoadCol1AndCol2(id).get();

        String logResult = smartLogger.toString(logEntity);

        System.err.println(logResult);


        // assertContainsIdOnce(logResult, child31.getId());
        // assertContainsIdOnce(logResult, child32.getId());
        assertContainsString(logResult, TOO_MANY_ENTRIES_STRING, 2);
        // assertContainsIdOnce(logResult, savedEagerSingleChild.getId());
        assertContainsStringOnce(logResult, LOG_ENTITY_NAME);
    }


    private void assertContainsStringOnce(String s, String subString) {
        Assertions.assertEquals(1, StringUtils.countMatches(s, subString));
    }

    private void assertContainsIdOnce(String s, Long id) {
        String refined = s
                .replace(LAZY_CHILD_NAME, "")
                .replace(EAGER_CHILD_NAME, "")
                .replace(COL3_ENTITY1_NAME, "")
                .replace(COL3_ENTITY2_NAME, "")
                .replace(LAZY_CHILD_NAME, "")
                .replace(LAZY_COL1_ENTITY1_NAME, "")
                .replace(LAZY_COL1_ENTITY2_NAME, "")
                .replace(LAZY_COL2_ENTITY1_NAME, "")
                .replace(LAZY_COL2_ENTITY2_NAME, "");

        if (id.equals(1L)) {
            assertContainsString(refined, id.toString(), 2);
        } else if (id.equals(2L)) {
            assertContainsString(refined, id.toString(), 2);
        } else {
            assertContainsStringOnce(refined, id.toString());
        }
    }


    private void assertContainsString(String s, String subString, Integer times) {
        Assertions.assertEquals(times, StringUtils.countMatches(s, subString));
    }

    private boolean isLoaded(Object parent, String childPropertyName) {
        PersistenceUnitUtil persistenceUtil =
                SmartLogger.getEntityManager().getEntityManagerFactory().getPersistenceUnitUtil();
        Boolean loaded = persistenceUtil.isLoaded(parent, childPropertyName);
        return loaded;
    }


    @AfterEach
    void tearDown() {
        TransactionalRapidTestUtil.clear(logChildService);
        TransactionalRapidTestUtil.clear(logChild2Service);
        TransactionalRapidTestUtil.clear(logChild3Service);
        TransactionalRapidTestUtil.clear(logChild4Service);
        TransactionalRapidTestUtil.clear(logParentService);
        TransactionalRapidTestUtil.clear(lazySingleLogChildService);
        TransactionalRapidTestUtil.clear(eagerSingleLogChildService);
        TransactionalRapidTestUtil.clear(logEntityService);
    }

}