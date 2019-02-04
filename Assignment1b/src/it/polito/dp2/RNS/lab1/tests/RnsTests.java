package it.polito.dp2.RNS.lab1.tests;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import it.polito.dp2.RNS.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;


public class RnsTests {
	protected static RnsReader referenceRnsReader;	// reference data generator
	protected static RnsReader testRnsReader;			// implementation under test
	protected static long testcase;
	
    class IdentifiedEntityReaderComparator implements Comparator<IdentifiedEntityReader> {
        public int compare(IdentifiedEntityReader f0, IdentifiedEntityReader f1) {
        	return f0.getId().compareTo(f1.getId());
        }
    }

	// method for comparing two strings that should be non-null    
	public void compareString(String rs, String ts, String meaning) {
		assertNotNull(rs);
		assertNotNull("null "+meaning, ts);
        assertEquals("wrong "+meaning, rs, ts);		
	}
	
	// method for comparing two time instants that should be non-null with precision of 1 minute
	public void compareTime(Calendar rc, Calendar tc, String meaning) {
		assertNotNull(rc);
		assertNotNull("null "+meaning, tc);
		
		// Compute lower and upper bounds for checking with precision of 1 minute
		Calendar upperBound, lowerBound;
		upperBound = (Calendar)rc.clone();
		upperBound.add(Calendar.MINUTE, 1);
		lowerBound = (Calendar)rc.clone();
		lowerBound.add(Calendar.MINUTE, -1);
		
		// Compute the condition to be checked
		boolean condition = tc.after(lowerBound) && tc.before(upperBound);
		
		assertTrue("wrong "+meaning, condition);
	}
		
	/**
	 * Starts the comparison of two sets of elements that extend IdentifiedEntityReader.
	 * This method already makes some comparisons that are independent of the type
	 * (e.g. the sizes of the sets must match). Then the method arranges the set
	 * elements into ordered sets (TreeSet) and returns a pair of iterators that
	 * can be used later on for one-to-one matching of the set elements
	 * @param rs	the first set to be compared
	 * @param ts	the second set to be compared
	 * @param type	a string that specified the type of data in the set (will appear in test messages)
	 * @return		a list made of two iterators to be used for one-to-one comparisons of the set elements
	 */
	public <T extends IdentifiedEntityReader> List<Iterator<T>> startComparison(Set<T> rs, Set<T> ts, String type) {
		// if one of the two sets is null while the other isn't null, the test fails
		if ((rs == null) && (ts != null) || (rs != null) && (ts == null)) {
		    fail("returned set of "+type+" was null when it should be non-null or vice versa");
		    return null;
		}

		// if both sets are null, there are no data to compare, and the test passes
		if ((rs == null) && (ts == null)) {
		    assertTrue("there are no "+type+"!", true);
		    return null;
		}
		
		// check that the number of elements matches
		assertEquals("wrong Number of "+type, rs.size(), ts.size());
		
		// create TreeSets of elements, using the comparator for sorting, one for reference and one for implementation under test 
		TreeSet<T> rts = new TreeSet<T>(new IdentifiedEntityReaderComparator());
		TreeSet<T> tts = new TreeSet<T>(new IdentifiedEntityReaderComparator());
   
		rts.addAll(rs);
		tts.addAll(ts);
		
		// get iterators and store them in a list
		List<Iterator<T>> list = new ArrayList<Iterator<T>>();
		list.add(rts.iterator());
		list.add(tts.iterator());
		
		// return the list
		return list;

	}

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    	// Create reference data generator
        System.setProperty("it.polito.dp2.RNS.RnsReaderFactory", "it.polito.dp2.RNS.Random.RnsReaderFactoryImpl");
        referenceRnsReader = RnsReaderFactory.newInstance().newRnsReader();

        // Create implementation under test       
        System.setProperty("it.polito.dp2.RNS.RnsReaderFactory", "it.polito.dp2.RNS.sol1.RnsReaderFactory");
        testRnsReader = RnsReaderFactory.newInstance().newRnsReader();
        
        // read testcase property
        Long testcaseObj = Long.getLong("it.polito.dp2.RNS.Random.testcase");
        if (testcaseObj == null)
        	testcase = 0;
        else
        	testcase = testcaseObj.longValue();
    }
    
    @Before
    public void setUp() throws Exception {
        assertNotNull("Internal tester error during test setup: null reference", referenceRnsReader);
        assertNotNull("Could not run tests: the implementation under test generated a null RnsReader", testRnsReader);
    }

    @Test
    // Check that getPlaces(null) returns the expected data
    public final void testGetPlaces() {
		// call getPlaces on the two implementations
		Set<PlaceReader> rps = referenceRnsReader.getPlaces(null);
		Set<PlaceReader> tps = testRnsReader.getPlaces(null);
		
		// compare the returned sets
		List<Iterator<PlaceReader>> list = startComparison(rps, tps, "Places");
		if (list!=null) {
			Iterator<PlaceReader> ri = list.get(0);
			Iterator<PlaceReader> ti = list.get(1);
			
			while (ri.hasNext() && ti.hasNext()) {
				PlaceReader rpr = ri.next();
				PlaceReader tpr = ti.next();
				comparePlaceReader(rpr,tpr);
				comparePlaceReader(rpr,testRnsReader.getPlace(rpr.getId()));
			}
		}
    }
    
    // private method for comparing two PlaceReader objects
	protected void comparePlaceReader(PlaceReader rsr, PlaceReader tsr) {
		// check the PlaceReaders are not null
		assertNotNull("internal tester error: null place reader", rsr);
        assertNotNull("unexpected null place reader", tsr);
        
        System.out.println("Comparing place "+rsr.getId());

        // check the PlaceReaders return the same data 
        compareString(rsr.getId(), tsr.getId(), "place id");       
		assertEquals("wrong place capacity", rsr.getCapacity(), tsr.getCapacity());
		List<Iterator<PlaceReader>> list = startComparison(rsr.getNextPlaces(), tsr.getNextPlaces(), "Next places");
		if (list!=null) {
			Iterator<PlaceReader> ri = list.get(0);
			Iterator<PlaceReader> ti = list.get(1);
			
			while (ri.hasNext() && ti.hasNext()) {
				PlaceReader rplace = ri.next();
				PlaceReader tplace = ti.next();
		        comparePlaceIdAndCapacity(rplace, tplace, "next place");
			}
		}
	}

	protected void comparePlaceIdAndCapacity(PlaceReader rplace, PlaceReader tplace, String meaning) {
		compareString(rplace.getId(), tplace.getId(), meaning+" id");       
		assertEquals("wrong "+meaning+" capacity", rplace.getCapacity(), tplace.getCapacity());
	}

    @Test
    public final void testGetVehicles() {
		// call getVehicles on the two implementations
		Set<VehicleReader> rvs = referenceRnsReader.getVehicles(null,null,null);
		Set<VehicleReader> tvs = testRnsReader.getVehicles(null,null,null);
		
		// compare the returned sets
		List<Iterator<VehicleReader>> list = startComparison(rvs, tvs, "Nffgs");
		if (list!=null) {
			Iterator<VehicleReader> ri = list.get(0);
			Iterator<VehicleReader> ti = list.get(1);
			
			while (ri.hasNext() && ti.hasNext()) {
				VehicleReader rvr = ri.next();
				VehicleReader tvr = ti.next();
				compareVehicleReader(rvr,tvr);
				compareVehicleReader(rvr,testRnsReader.getVehicle(rvr.getId()));
			}
		}		
    }
    
    // private method for comparing two VehicleReader objects
	protected void compareVehicleReader(VehicleReader rvr, VehicleReader tvr) {
		// check the VehicleReaders are not null
		assertNotNull("internal tester error: null vehicle reader", rvr);
        assertNotNull("unexpected null vehile reader", tvr);
        
        System.out.println("Comparing vehicle "+rvr.getId());

        // check the VehicleReaders return the same data
        compareString(rvr.getId(), tvr.getId(), "vehicle id");
		if (testcase > 2) // time checking only in this case
			compareTime(rvr.getEntryTime(), tvr.getEntryTime(), "entry time");
		comparePlaceIdAndCapacity(rvr.getDestination(),tvr.getDestination(),"destination");
		comparePlaceIdAndCapacity(rvr.getOrigin(),tvr.getOrigin(),"origin");
		comparePlaceIdAndCapacity(rvr.getPosition(),tvr.getPosition(),"position");
		assertEquals("wrong state", rvr.getState(), tvr.getState());
	}

    @Test
    public final void testGetGates() {
		// call getGates on the two implementations
		Set<GateReader> rps = referenceRnsReader.getGates(null);
		Set<GateReader> tps = testRnsReader.getGates(null);
		
		// compare the returned sets
		List<Iterator<GateReader>> list = startComparison(rps, tps, "Gates");
		if (list!=null) {
			Iterator<GateReader> ri = list.get(0);
			Iterator<GateReader> ti = list.get(1);
			
			while (ri.hasNext() && ti.hasNext()) {
				GateReader rpr = ri.next();
				GateReader tpr = ti.next();
				compareGateReader(rpr,tpr);
			}
		}
    }

	protected void compareGateReader(GateReader rpr, GateReader tpr) {
		comparePlaceReader(rpr,tpr);
		assertEquals("wrong gate type", rpr.getType(), tpr.getType());
	}

    @Test
    public final void testGetRoadSegments() {
		// call getRoadSegments on the two implementations
		Set<RoadSegmentReader> rps = referenceRnsReader.getRoadSegments(null);
		Set<RoadSegmentReader> tps = testRnsReader.getRoadSegments(null);
		
		// compare the returned sets
		List<Iterator<RoadSegmentReader>> list = startComparison(rps, tps, "Road segments");
		if (list!=null) {
			Iterator<RoadSegmentReader> ri = list.get(0);
			Iterator<RoadSegmentReader> ti = list.get(1);
			
			while (ri.hasNext() && ti.hasNext()) {
				RoadSegmentReader rpr = ri.next();
				RoadSegmentReader tpr = ti.next();
				compareRoadSegmentReader(rpr,tpr);
			}
		}
    }

	protected void compareRoadSegmentReader(RoadSegmentReader rpr, RoadSegmentReader tpr) {
		comparePlaceReader(rpr,tpr);
		compareString(rpr.getName(),tpr.getName(),"road segment name");
		compareString(rpr.getRoadName(),tpr.getRoadName(),"road name");
	}

    @Test
    public final void testGetParkingAreas() {
		// call getGates on the two implementations
		Set<ParkingAreaReader> rps = referenceRnsReader.getParkingAreas(null);
		Set<ParkingAreaReader> tps = testRnsReader.getParkingAreas(null);
		
		// compare the returned sets
		List<Iterator<ParkingAreaReader>> list = startComparison(rps, tps, "Parking Areas");
		if (list!=null) {
			Iterator<ParkingAreaReader> ri = list.get(0);
			Iterator<ParkingAreaReader> ti = list.get(1);
			
			while (ri.hasNext() && ti.hasNext()) {
				ParkingAreaReader rpr = ri.next();
				ParkingAreaReader tpr = ti.next();
				compareParkingAreaReader(rpr,tpr);
			}
		}
    }

	protected void compareParkingAreaReader(ParkingAreaReader rpr, ParkingAreaReader tpr) {
		comparePlaceReader(rpr,tpr);
		
		// compare services
		Set<String> rss = rpr.getServices();
		Set<String> tss = tpr.getServices();
		
		// if set returned by implementation under test is null the test fails
		if (tss == null) {
		    fail("returned set of services was null");
		}

		// check that the number of elements matches
		assertEquals("wrong Number of services", rss.size(), tss.size());
		
		// create TreeSets of elements, using natural sorting, one for reference and one for implementation under test 
		TreeSet<String> rts = new TreeSet<String>();
		TreeSet<String> tts = new TreeSet<String>();
   
		rts.addAll(rss);
		tts.addAll(tss);
		
		// get iterators
		Iterator<String> ri = rts.iterator();
		Iterator<String> ti = tts.iterator();
					
		while (ri.hasNext() && ti.hasNext())
			compareString(ri.next(),ti.next(),"service");

	}

}
