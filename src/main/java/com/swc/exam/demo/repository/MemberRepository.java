package com.swc.exam.demo.repository;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.swc.exam.demo.vo.Member;

@Mapper
public interface MemberRepository {

	@Insert("""
			INSERT INTO `member`
			SET regDate = NOW(),
			updateDate = NOW(),
			loginId = #{loginId},
			loginPw = #{loginPw},
			`name` = #{name},
			nickname = #{nickname},
			cellphoneNo = #{cellphoneNo},
			email = #{email},
			favoriteCinema = #{favoriteCinema}
						""")
	void join( String loginId, String loginPw, String name,
			 String nickname, String cellphoneNo, String email, int favoriteCinema);

	@Select("SELECT LAST_INSERT_ID()")
	int getLastInsertId();

	@Select("""
			SELECT *
			FROM `member` AS M
			WHERE M.id = #{id}
			AND delStatus = 0
						""")
	Member getMemberById( int id);

	@Select("""
			SELECT *
			FROM `member` AS M
			WHERE M.loginId = #{loginId}
			AND delStatus = 0
						""")
	Member getMemberByLoginId( String loginId);

	@Select("""
			SELECT *
			FROM `member` AS M
			WHERE M.name = #{name}
			AND M.email = #{email}
			""")
	Member getMemberByNameAndEmail(String name, String email);

	@Update("""
			<script>
			UPDATE `member`
			<set>
				updateDate = NOW(),
				<if test="loginPw != null">
					loginPw = #{loginPw},
				</if>
				<if test="name != null">
					name = #{name},
				</if>
				<if test="nickname != null">
					nickname = #{nickname},
				</if>
				<if test="cellphoneNo != null">
					cellphoneNo = #{cellphoneNo},
				</if>
				<if test="email != null">
					email = #{email},
				</if>
				<if test="favoriteCinema != null">
					favoriteCinema = #{favoriteCinema},
				</if>
			</set>
			WHERE id = #{id}
			</script>
			""")
	void modify(int id, String loginPw, String name, String nickname, String cellphoneNo, String email,
			 int favoriteCinema);

	@Select("""
			<script>
			SELECT COUNT(*) AS cnt
			FROM `member` AS M
			WHERE 1
			AND delStatus = 0
			<if test="authLevel != 0">
				AND M.authLevel = #{authLevel}
			</if>
			<if test="searchKeyword != ''">
				<choose>
					<when test="searchKeywordTypeCode == 'loginId'">
			    		AND M.loginId LIKE CONCAT('%', #{searchKeyword}, '%')
			    	</when>
			    	<when test="searchKeywordTypeCode == 'name'">
			    		AND M.name LIKE CONCAT('%', #{searchKeyword}, '%')
			    	</when>
			    	<when test="searchKeywordTypeCode == 'nickname'">
			    		AND M.nickname LIKE CONCAT('%', #{searchKeyword}, '%')
			    	</when>
					<otherwise>
						AND(
							M.loginId LIKE CONCAT('%', #{searchKeyword}, '%')
							OR
							M.name LIKE CONCAT('%', #{searchKeyword}, '%')
							OR
							M.nickname LIKE CONCAT('%', #{searchKeyword}, '%')
							)
					</otherwise>
				</choose>
			</if>
			</script>
			""")
	int getMembersCount(int authLevel, String searchKeywordTypeCode, String searchKeyword);

	@Select("""
			<script>
			SELECT M.*
			FROM `member` AS M
			WHERE 1
			AND delStatus = 0
			<if test="authLevel != 0">
				AND M.authLevel = #{authLevel}
			</if>
			<if test="searchKeyword != ''">
				<choose>
					<when test="searchKeywordTypeCode == 'loginId'">
			    		AND M.loginId LIKE CONCAT('%', #{searchKeyword}, '%')
			    	</when>
			    	<when test="searchKeywordTypeCode == 'name'">
			    		AND M.name LIKE CONCAT('%', #{searchKeyword}, '%')
			    	</when>
			    	<when test="searchKeywordTypeCode == 'nickname'">
			    		AND M.nickname LIKE CONCAT('%', #{searchKeyword}, '%')
			    	</when>
					<otherwise>
						AND(
							M.loginId LIKE CONCAT('%', #{searchKeyword}, '%')
							OR
							M.name LIKE CONCAT('%', #{searchKeyword}, '%')
							OR
							M.nickname LIKE CONCAT('%', #{searchKeyword}, '%')
							)
					</otherwise>
				</choose>
			</if>
			ORDER BY M.id DESC
			<if test="limitStart != -1">
			LIMIT #{limitStart}, #{limitTake}
			</if>
			</script>
			""")
	List<Member> getForPrintMembers(int authLevel, String searchKeywordTypeCode, String searchKeyword, int limitStart,
			int limitTake);

	
	@Update("""
			<script>
			UPDATE `member`
			<set>
				delStatus = 1,
				delDate = NOW()
			</set>
			WHERE id = #{id}
			</script>
			""" )
	void deleteMember(int id);

	
	@Select("""
			SELECT favoriteCinema
			FROM `member`
			WHERE id = #{id}			
			""")
	int getMemberFavoriteCinema(int id);
}
