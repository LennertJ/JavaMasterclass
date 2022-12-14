package com.crm.miniCRM.controller;

import com.crm.miniCRM.dto.MemberDto;
import com.crm.miniCRM.dto.PersonDto;
import com.crm.miniCRM.model.Community;
import com.crm.miniCRM.model.Member;
import com.crm.miniCRM.model.Person;
import com.crm.miniCRM.model.persistence.helpers.MemberID;
import com.crm.miniCRM.model.persistence.interfaces.CommunityRepository;
import com.crm.miniCRM.model.persistence.interfaces.MemberRepository;
import com.crm.miniCRM.model.persistence.interfaces.PersonRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping(value = "/members")
public class MemberController {
    private final MemberRepository memberService;
    private final PersonRepository personService;
    private final CommunityRepository communityService;

    public MemberController(MemberRepository memberService, PersonRepository personService,CommunityRepository communityService) {
        this.memberService = memberService;
        this.personService = personService;
        this.communityService = communityService;
    }

    @GetMapping
    public String getmembers(Model model) {
        Iterable<Member> members = memberService.findAll();
        List<MemberDto> memberDtos = new ArrayList<>();
        members.forEach(m-> {
            boolean communityExists = false;
            MemberID memberID = m.getId();
            Long personId = memberID.getPerson_ID();
            Person person = this.personService.findById(personId).orElse(null);
            if(person ==null){
                return;
            }
            Long communityId = memberID.getCommunity_ID();
            for (MemberDto memberdto: memberDtos) {
                if(Objects.equals(memberdto.getCommunity().getID(), communityId)){
                    memberdto.addPersonToCommunity(person);
                    communityExists = true;
                }
            }
            if(!communityExists){
                Community c = communityService.findById(communityId).orElse(null);
                if(c!=null){
                    memberDtos.add(new MemberDto(c, person, m.getSince(),m.getUntil()));
                }
                else{
                    System.out.println("Community does not exist");
                }
            }
        });
        model.addAttribute("members", memberDtos);
        return "members";
    }

    @GetMapping("/editMembers/{id}")
    public String editPerson(Model model,@PathVariable("id") Long id) {
        ArrayList<MemberDto> memberDtos = CreateMemberDtoFromCommunity(id);
        assert memberDtos != null;
        model.addAttribute("communityMembers", memberDtos.get(0).getPersons());
        model.addAttribute("nonCommunityMembers", memberDtos.get(1).getPersons());
        return "edit-members";
        //return null;
    }

    private ArrayList<MemberDto> CreateMemberDtoFromCommunity(Long id) {
        ArrayList<Member> members = (ArrayList<Member>) memberService.findAll();
        Community c = communityService.findById(id).orElse(null);
        if(c==null){
            return null;
        }
        MemberDto memberDto = new MemberDto(c);
        MemberDto nonMemberDto = new MemberDto(c);
        ArrayList<Long> personsAdded = new ArrayList<>();
        Person person;
        for (Member member: members) {
            if(Objects.equals(member.getId().getCommunity_ID(), memberDto.getCommunity().getID())){
                person = personService.findById(member.getId().getPerson_ID()).orElse(null);
                if(person!=null){
                    if(!personsAdded.contains(person.getId()) ){
                        memberDto.addPersonToCommunity(person);
                        personsAdded.add(person.getId());
                    }
                }
            }else{
                person = personService.findById(member.getId().getPerson_ID()).orElse(null);
                if(person!=null){
                    if(!personsAdded.contains(person.getId()) ){
                        nonMemberDto.addPersonToCommunity(person);
                        personsAdded.add(person.getId());
                    }

                }
            }
        }
        ArrayList<MemberDto> memberDtos = new ArrayList<>();
        memberDtos.add(memberDto);
        memberDtos.add(nonMemberDto);
        return memberDtos;
    }
}
