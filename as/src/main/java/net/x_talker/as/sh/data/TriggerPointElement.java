package net.x_talker.as.sh.data;

import java.util.Iterator;
import java.util.Vector;

public class TriggerPointElement {
	private int conditionTypeCNF = 1;
	private Vector<SPTElement> sptList = null;

	public TriggerPointElement() {
	}

	public String toString() {
		StringBuffer sBuffer = new StringBuffer();
		sBuffer.append(ShDataTags.TriggerPoint_s);

		sBuffer.append(ShDataTags.ConditionTypeCNF_s);
		sBuffer.append(conditionTypeCNF);
		sBuffer.append(ShDataTags.ConditionTypeCNF_e);

		if (sptList != null && sptList.size() > 0) {
			Iterator<SPTElement> it = sptList.iterator();
			while (it.hasNext()) {
				SPTElement spt = it.next();
				sBuffer.append(spt.toString());
			}
		}
		sBuffer.append(ShDataTags.TriggerPoint_e);
		return sBuffer.toString();
	}

	public void addSPT(SPTElement spt) {
		if (sptList == null) {
			sptList = new Vector<SPTElement>();
		}
		sptList.add(spt);

	}

	public int getConditionTypeCNF() {
		return conditionTypeCNF;
	}

	public void setConditionTypeCNF(int conditionTypeCNF) {
		this.conditionTypeCNF = conditionTypeCNF;
	}

	public Vector<SPTElement> getSptList() {
		return sptList;
	}

	public void setSptList(Vector<SPTElement> sptList) {
		this.sptList = sptList;
	}
}
