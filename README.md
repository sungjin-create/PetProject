# 🐶 펫 관리 및 커뮤니티 서비스
반려동물을 관리하고 커뮤니티를 통해 사람들과 반려동물과의 추억, 정보들을 공유할 수 있는 서비스입니다

## 프로젝트 기능
- 회원 관리
  - 회원가입
    - 이메일로 회원가입이 가능합니다
    - 이메일은 unique 해야합니다.
    - 네이버, 카카오, 구글의 계정으로 회원가입이 가능합니다
  - 로그인
    - 인증된 이메일과 패스워드로 로그인 가능합니다
    - 로그인시 회원가입때 사용한 아이디와 패스워드가 일치해야 합니다.
    - 네이버, 카카오, 구글의 계정으로 로그인 가능합니다

- 반려동물 등록
  - 로그인한 사용자는 반려동물을 등록할 수 있습니다.
  - 반려동물의 정보는 수정할 수 있습니다.
  - 나이, 이름(별명)을 등록 할 수 있습니다
  - 여러마리 등록이 가능합니다
  - 등록된 동물마다 각각 관리가 가능합니다
 
- 반려동물 관리 기능
  - 로그인한 사용자는 반려동물 관리 기능을 사용할 수 있습니다.
  - 사료
    - 사료 제공 양, 제공 주기를 등록할 수 있습니다 (ex 100g씩 아침 저녁)
    - 사료 제공 여부는 체크리스트로 관리합니다
    - 알람 기능을 설정할 수 있습니다(ex 설정한 기한 30분전)
  - 검강검진
    - 건강검진 주기를 등록할 수 있습니다
    - 건강검진 방문 여부 체크리스트로 관리할 수 있습니다
    - 알람 기능을 설정 할 수 있습니다(ex 설정한 기한 하루전)
  - Todo리스트
    - Todo리스트를 설정할 수 있습니다
    - ex) 산책, 간식, 반려동물 용품 구매 등에 활용될 수 있습니다
 
- 커뮤니티 기능
  - 로그인한 사용자는 커뮤니티 기능을 사용할 수 있습니다.
  - 커뮤니티 기능 개요
    1. 전체 게시글 조회 (최신순, 좋아요 순으로 조회 가능) 할 수 있습니다
    2. 팔로우한 계정들의 게시물을 볼수 있습니다
    3. 검색어에 따른 게시물을 볼수 있습니다
    4. 나의 게시물을 모아서 볼수 있습니다
  - 기능
    - 게시물 작성 (등록, 수정, 삭제)이 가능합니다
      - 로그인한 사용자는 글을 작성 할 수 있습니다.
    - 팔로우
      - 팔로우한 사람들의 목록을 볼수 있습니다
      - 팔로우 목록에서 팔로우를 취소할 수 있습니다
    - 좋아요
      - 좋아요는 계정당 같은 게시물에 한번만 반영됩니다
      - 좋아요를 취소 할수 있습니다
    - 댓글
      - 특정 게시글을 조회할때 댓글을 조회할 수 있습니다.
      - 댓글이 많을 수 있기에 별도의 API로 구성합니다.
      - 댓글은 최신순으로만 정렬되며, paging 처리합니다.
      - 댓글은 작성자, 내용, 작성일이 모두에게 보여집니다.
# ERD
![image](https://github.com/sungjin-create/PetProject/assets/49832261/a16a9f9f-0fc7-462f-b387-b7faf1ac340b)





# Trouble Shooting
[go to the trouble shooting section](https://github.com/sungjin-create/PetProject/blob/main/doc/TROUBLE_SHOOTING.md)

# 사용 기술(Tech Stack)
<div align=center> 
  <img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white"> 
  <img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white"> 
  <img src="https://img.shields.io/badge/spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white"> 
  <img src="https://img.shields.io/badge/git-F05032?style=for-the-badge&logo=git&logoColor=white">
</div>
