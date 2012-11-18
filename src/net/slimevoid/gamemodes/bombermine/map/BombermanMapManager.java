package net.slimevoid.gamemodes.bombermine.map;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BombermanMapManager {
	
	public BombermanMapManager() {
		ignoredMaps = new ArrayList<String>();
		
		parseIgnoredMaps(BombermanMap.ignoredMaps);
		mapGettingProcess = MapGettingProcess.values()[BombermanMap.mapChangeMode];
		
		mapList = new ArrayList<BombermanMap>() {
			private static final long serialVersionUID = 5132989329560781873L;

			@Override
			public boolean add(BombermanMap map) {
				if(ignoredMaps.contains(map.getName())) {
					return false;
				} else {
					return super.add(map);
				}
			}
		};
		readMaps();
	}
	
	public void readMaps() {
		File folder = new File("maps");
		if(!folder.exists()) {
			folder.mkdir();
		}
		
		mapList.clear();
		
		for(File mapFile : listMap(folder)) {
			BombermanMap map = parseMap(mapFile);
			mapList.add(map);
		}
	}
	
	public BombermanMap getNextMap() {
		switch(mapGettingProcess) {
		case random:
			int rd = rand.nextInt(mapList.size());
			lastMap = mapList.get(rd);
			break;
			
		case normal:
			int index = mapList.indexOf(lastMap);
			if(index == mapList.size()-1) {
				index = 0;
			} else {
				index ++;
			}
			lastMap = mapList.get(index);
			break;
		}
		System.out.println("Next map: "+lastMap.getName()+" ("+mapList+")");
		return lastMap;
	}
	
	private List<File> listMap(File folder) {
		List<File> listFiles = new ArrayList<File>();
		FilenameFilter filter = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".map");
			}
		};
		
		for(File file : folder.listFiles(filter)) {
			listFiles.add(file);
		}
		
		return listFiles;
	}
	
	private BombermanMap parseMap(File file) {
		BufferedInputStream in = null;
		try {
			String name = "", nameAutor = "", comment = "";
			int nbGens = 0;
			List<MapGeneratorFunc> generators = new ArrayList<MapGeneratorFunc>();
			
			in = new BufferedInputStream(new FileInputStream(file));
			
//			1 byte = 5 -> Start Bomberman Map
			if(in.available() < 456 || in.read() != 5) {
				return null;
			}
			
			byte[] bytes = new byte[32];
			
//			32 bytes : name
			in.read(bytes, 0, 32);
			name = new String(bytes).trim();
			
//			32 bytes : author
			in.read(bytes, 0, 32);
			nameAutor = new String(bytes).trim();

			bytes = new byte[255];
//			255 bytes : comment
			in.read(bytes, 0, 255);
			comment = new String(bytes).trim();
//			128 bytes : RESERVED
			in.skip(128);

//			1 byte = 7 -> Start Gen List
			if(in.read() != 7) {
				return null;
			}
			
//			1 byte = Number of Gens
			nbGens = in.read();
			
//			[GEN List]
			for(int i = 0; i < nbGens; i++) {
				if(in.available() < 23) {
					return null;
				}
				byte id, xPos, zPos, xSize, zSize, blockID;
				boolean flag;
//			1 byte : Gen Type > 1 = #GenRandom
//			                    2 = #GenBox
//			                    3 = #GenCross
//			                    4 = #GenLine
//			                    5 = #GenDot
				id = (byte) in.read();
//			1 byte : x Position
				xPos = (byte) in.read();
//			1 byte : z Position
				zPos = (byte) in.read();
//			1 byte : x Size
				xSize = (byte) in.read();
//			1 byte : z Size
				zSize = (byte) in.read();
//			1 byte : Flag
				flag = in.read() > 0;
//			1 byte : Block Type
				blockID = (byte) in.read();
//			16 Bytes : RESERVED
				in.skip(16);
				
				MapGeneratorFunc generator = MapGeneratorFunc.getGenerator(id, xPos, zPos, xSize, zSize, blockID, flag);
				
				if(generator == null) {
					return null;
				}
				
				generators.add(generator);
			}
//			1 byte = 13 -> End Gen List
			if(in.available() < 5 || in.read() != 13) {
				return null;
			}
//			1 byte = 9 -> Start Spawns
			if(in.read() != 9) {
				return null;
			}
//			1 byte : Gen Spawn type > 0 = Static
//			                          1 = Dynamic
//			1 byte = Number of Spawns (0 means Dynamic spawn Gen)
			in.skip(2);
//			[SPAWN List]
//			1 byte : x Position
//			1 byte : z Position
//			1 byte : Flag
//			16 Bytes : RESERVED
//
//			1 byte = 19 ->End Spawns
			if(in.read() != 19) {
				return null;
			}  
//			                          
//			1 byte = 11 -> End Bomberman Map
			if(in.read() != 11) {
				return null;
			}
			if(in.read() != -1) {
				return null;
			}
			
			BombermanMap map = new BombermanMap(name, nameAutor, comment, generators);
			for(MapGeneratorFunc generator : map.getGens()) {
				generator.setMap(map);
			}
			return map;
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
	private void parseIgnoredMaps(String in) {
		String[] maps = in.split(",");
		for(String map : maps) {
			map.trim();
			if(map == null || map == "") {
				continue;
			}
			System.out.println(map +" will be ignored");
			ignoredMaps.add(map);
		}
	}
	
	public void addIngoredMaps(List<String> ingoredMaps) {
		this.ignoredMaps.addAll(ingoredMaps);
	}

	private List<BombermanMap> mapList;
	private BombermanMap lastMap = null;
	private MapGettingProcess mapGettingProcess = MapGettingProcess.random;
	private final List<String> ignoredMaps;
	
	private static final Random rand = new Random();
	
	private enum MapGettingProcess {
		random, normal;
	}
}
