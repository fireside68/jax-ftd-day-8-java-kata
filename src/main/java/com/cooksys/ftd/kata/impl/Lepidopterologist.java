package com.cooksys.ftd.kata.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

import com.cooksys.ftd.kata.ILepidopterologist;
import com.cooksys.ftd.kata.model.Butterpillar;
import com.cooksys.ftd.kata.model.Catterfly;
import com.cooksys.ftd.kata.model.GrowthModel;
import com.cooksys.ftd.kata.model.Sample;
import com.cooksys.ftd.kata.model.Species;

public class Lepidopterologist implements ILepidopterologist {
	
	Map<Species, List<Sample>> thisMap = new TreeMap<Species, List<Sample>>();
	
	@Override
	public List<Sample> getSamplesForSpecies(Species species) {
		List<Sample> list = new ArrayList<Sample>();
		if (thisMap.containsKey(species)) {
			list = thisMap.get(species);
		}
		return list;
	}

	@Override
	public List<Species> getRegisteredSpecies() {
		List<Species> list = new ArrayList<Species>();
		for(Map.Entry<Species, List<Sample>> entry : thisMap.entrySet())
			list.add(entry.getKey());	
		return list;
		
	}

	@Override
	public boolean registerSpecies(Species species) {
		if(!isSpeciesRegistered(species)){
			thisMap.put(species, new ArrayList<Sample>());
			return true;
			}
		return false;
	}

	@Override
	public boolean isSpeciesRegistered(Species species) {
		return thisMap.containsKey(species);
	}

	@Override
	public Optional<Species> findSpeciesForSample(Sample sample) {
		for(Map.Entry<Species, List<Sample>> entry : thisMap.entrySet()) {
			  Species key = entry.getKey();
			  GrowthModel growthModel = key.getGrowthModel();
			  Butterpillar b = sample.getButterpillar();
			  Catterfly c = growthModel.convert(b);
			  Sample compare = new Sample(b, c);  
			  if (compare.equals(sample))
				  return Optional.ofNullable(key);
			}
		return Optional.ofNullable(null);
	}

	@Override
	public boolean recordSample(Sample sample) {
		Optional<Species> key = findSpeciesForSample(sample);
		if (key.isPresent()) {
			if (!thisMap.containsKey(key.get()))
				return false;
			List<Sample> list = getSamplesForSpecies(key.get());
			list.add(sample);
			return true;
		}
		return false;
	}


	@Override
	public Map<Species, List<Sample>> getTaxonomy(){
		for(Map.Entry<Species, List<Sample>> entry : thisMap.entrySet())
			Collections.sort(entry.getValue());
		return thisMap;
	}

}
