package cosmos;





public class Block {
	public enum BlockTypes {
	    DEFAULT,GRASS,DIRT,WATER,STONE,WOOD,SAND
	}//more in future
	

	    private boolean active;
	    private BlockTypes blockType;

	    public Block() {
	        this.active = false;//temp
	        this.blockType = BlockTypes.DEFAULT;
	    }



		public boolean isActive() {
	        return active;
	    }

	    public void setActive(boolean active) {
	        this.active = active;
	    }

	    public BlockTypes getBlockType() {
	        return blockType;
	    }

	    public void setBlockType(BlockTypes blockType) {
	        this.blockType = blockType;
	    }
	    
	    
	

}


    

