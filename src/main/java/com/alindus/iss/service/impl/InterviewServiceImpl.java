package com.alindus.iss.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.alindus.iss.domain.Client;
import com.alindus.iss.domain.Interview;
import com.alindus.iss.domain.InterviewRound;
import com.alindus.iss.domain.InterviewType;
import com.alindus.iss.domain.Round;
import com.alindus.iss.domain.Technology;
import com.alindus.iss.domain.Vendor;
import com.alindus.iss.repository.InterviewRepository;
import com.alindus.iss.service.InterviewService;

@Service
public class InterviewServiceImpl implements InterviewService {

	@Autowired
	private InterviewRepository interviewRepository;

	@Override
	@Transactional
	public void add(Interview t) {
		if (t.getId() != null) {
			throw new IllegalArgumentException("Invalid interview data.");
		}
		Client client = this.interviewRepository.findClientByName(t.getClient().getName());
		if (client != null)
			t.setClient(client);
		Technology technology = this.interviewRepository.findTechnologyByName(t.getTechnology().getName());
		if (technology != null)
			t.setTechnology(technology);
		Vendor vendor = this.interviewRepository.findVendorByName(t.getVendor().getName());
		if (vendor != null)
			t.setVendor(vendor);
		for (InterviewRound ir : t.getInterviewRound()) {
			InterviewType interviewType = this.interviewRepository
					.findInterviewTypeByType(ir.getInterviewType().getType());
			if (interviewType != null)
				ir.setInterviewType(interviewType);
			Round round = this.interviewRepository.findRoundByName(ir.getRound().getName());
			if (round != null)
				ir.setRound(round);
			ir.setInterview(t);
			ir.setCreatedDate(new Date());
		}
		t.setCreatedDate(new Date());
		this.interviewRepository.save(t);

	}

	@Override
	@Transactional
	public void update(Interview t) {
		if (t.getId() == null) {
			throw new IllegalArgumentException("Invalid interview data.");
		}
		Interview interview = this.interviewRepository.findOne(t.getId());
		if (interview == null) {
			throw new IllegalArgumentException("Interview does not exist.");
		}
		// update interview
		Client client = this.interviewRepository.findClientByName(t.getClient().getName());
		if (client != null)
			t.setClient(client);
		Technology technology = this.interviewRepository.findTechnologyByName(t.getTechnology().getName());
		if (technology != null)
			t.setTechnology(technology);
		Vendor vendor = this.interviewRepository.findVendorByName(t.getVendor().getName());
		if (vendor != null)
			t.setVendor(vendor);
		Interview newInterview = null;
		// Update interview round
		List<InterviewRound> list = new ArrayList<>();
		if (t.getInterviewRound() != null)
			list = updateInterviewRound(t, interview);
		newInterview = new Interview(t.getCandidate(), t.getClient(), t.getVendor(), t.getVc(), t.getMarketing(),
				t.getTechnology(), list, t.getStatus());
		newInterview.setUpdatedDate(new Date());
		newInterview.setId(interview.getId());
		this.interviewRepository.save(newInterview);

	}

	/**
	 * check the size of both list check if elements in old present in new list
	 * if present in new list update values by new list if not present call the
	 * remove interview round from the repository return the interview round
	 * list
	 */
	private List<InterviewRound> updateInterviewRound(Interview newInterview, Interview oldInterview) {
		List<InterviewRound> irToReturn = new ArrayList<>();
		// Create InterviewRound list to remove
		List<InterviewRound> irToRemove = new ArrayList<>();
		for (InterviewRound ir : oldInterview.getInterviewRound()) {
			if (!newInterview.getInterviewRound().contains(ir))
				irToRemove.add(ir);
		}
		// Remove Interview Round which is updated from new list
		for (InterviewRound rd : irToRemove)
			this.interviewRepository.removeInterviewRound(rd.getId());
		// Split modified and new InterviewRound list
		List<InterviewRound> modifiedIRList = new ArrayList<>();
		List<InterviewRound> newIRList = new ArrayList<>();
		for (InterviewRound ir : newInterview.getInterviewRound()) {
			if (oldInterview.getInterviewRound().contains(ir))
				modifiedIRList.add(ir);
			else
				newIRList.add(ir);
		}
		InterviewRound newIR = null;
		InterviewRound oldIR = null;
		for (InterviewRound iRound : modifiedIRList) {
			for (InterviewRound oIR : oldInterview.getInterviewRound()) {
				if (iRound.getId().equals(oIR.getId())) {
					oldIR = oIR;
					break;
				}
			}
			newIR = new InterviewRound(iRound.getRound(), iRound.getStatus(), iRound.getInterviewType(),
					iRound.getInterviewDate(), oldIR.getInterview(), iRound.getCallTaker());
			InterviewType interviewType = this.interviewRepository
					.findInterviewTypeByType(iRound.getInterviewType().getType());
			if (interviewType != null)
				newIR.setInterviewType(interviewType);
			Round round = this.interviewRepository.findRoundByName(iRound.getRound().getName());
			if (round != null)
				newIR.setRound(round);
			newIR.setId(iRound.getId());
			newIR.setCreatedDate(oldIR.getCreatedDate());
			newIR.setUpdatedDate(new Date());
			irToReturn.add(newIR);
		}
		for (InterviewRound nIR : newIRList) {
			InterviewType interviewType = this.interviewRepository
					.findInterviewTypeByType(nIR.getInterviewType().getType());
			if (interviewType != null)
				nIR.setInterviewType(interviewType);
			Round round = this.interviewRepository.findRoundByName(nIR.getRound().getName());
			if (round != null)
				nIR.setRound(round);
			nIR.setCreatedDate(new Date());
			irToReturn.add(nIR);
		}
		return irToReturn;
	}

	@Override
	@Transactional
	public void remove(Long obj) {
		if (obj == null) {
			throw new IllegalArgumentException("Invalid primary key.");
		}
		this.interviewRepository.delete(obj);
	}

	@Override
	public Interview findOne(Long obj) {
		if (obj == null) {
			throw new IllegalArgumentException("Invalid primary key.");
		}
		return this.interviewRepository.findOne(obj);
	}

	@Override
	public List<Interview> findAll() {
		return this.interviewRepository.findAll();
	}

	@Override
	public List<Client> findAllClients() {
		return this.interviewRepository.findAllClient();
	}

	@Override
	public List<Client> findClientsByNameLike(String name) {
		if (name == null) {
			throw new IllegalArgumentException("Invalid data.");
		}
		return this.interviewRepository.findClientsByNameLike(name);
	}

	@Override
	public List<Vendor> findAllVendors() {
		return this.interviewRepository.findAllVendor();
	}

	@Override
	public List<Vendor> findVendorsByNameLike(String name) {
		if (name == null) {
			throw new IllegalArgumentException("Invalid data.");
		}
		return this.interviewRepository.findVendorsByNameLike(name);
	}

	@Override
	public List<Technology> findAllTechnologies() {
		return this.interviewRepository.findAllTechnology();
	}

	@Override
	public List<Technology> findTechnologiesByNameLike(String name) {
		if (name == null) {
			throw new IllegalArgumentException("Invalid data.");
		}
		return this.interviewRepository.findTechnologiesByNameLike(name);
	}

	@Override
	public List<Round> findAllRounds() {
		return this.interviewRepository.findAllRound();
	}

	@Override
	public List<Round> findRoundsByNameLike(String name) {
		if (name == null) {
			throw new IllegalArgumentException("Invalid data.");
		}
		// results.stream().forEach((record) -> {
		// Round round = new Round((Long.parseLong(record[0].toString())),
		// (String) record[1]);
		// list.add(round);
		// });
		return this.interviewRepository.findRoundByNameLike(name);
	}

	@Override
	public List<InterviewType> findAllInterviewTypes() {
		return this.interviewRepository.findAllInterviewType();
	}

	@Override
	public List<InterviewType> findInterviewsTypeByTypeLike(String type) {
		if (type == null) {
			throw new IllegalArgumentException("Invalid data.");
		}
		return this.interviewRepository.findInterviewsTypeByTypeLike(type);
	}

	@Override
	@Transactional
	public void updateInterviewRound(InterviewRound interviewRound) {
		if (interviewRound.getId() == null || interviewRound.getInterview() == null) {
			throw new IllegalArgumentException("Invalid data.");
		}
		this.interviewRepository.updateInterviewRound(interviewRound.getId(), interviewRound.getRound(),
				interviewRound.getStatus(), interviewRound.getInterviewType(), interviewRound.getInterviewDate(),
				interviewRound.getInterview(), interviewRound.getCallTaker());
	}

	@Override
	public List<InterviewRound> findInterviewRounds() {
		return this.interviewRepository.getInterviewRounds();
	}

	@Override
	public List<InterviewRound> findInterviewRoundsByInterviewId(Long id) {
		Interview interview = this.interviewRepository.findOne(id);
		return this.interviewRepository.getInterviewRoundsByInterviewId(interview);
	}

	@Override
	public Page<Interview> findAll(Pageable pageable) {
		return this.interviewRepository.findAll(this.createPageRequest());
	}

	private Pageable createPageRequest() {
		return new PageRequest(1, 20, Sort.Direction.DESC, "id", "createdDate");
	}

}
