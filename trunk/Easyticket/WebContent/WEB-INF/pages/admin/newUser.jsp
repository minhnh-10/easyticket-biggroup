<%@ include file="template.jsp" %>
<%@include file="/common/taglibs.jsp" %>

<section id="main" class="column">

<article class="module width_full">
	<header><h3>New User</h3></header>
	<form action="createUser" method="post" enctype="multipart/form-data">
		<div class="module_content">
				<fieldset style="width:48%; float:left; margin-right: 3%;">
					<label>UserName</label>
					<input type="text" style="width:92%;" name="userName" value='<s:property value="userName" />' >
				</fieldset>
				<fieldset style="width:48%; float:left;">
					<label>Full Name</label>
					<input type="text" style="width:92%;" name="fullName" value='<s:property value="fullName" />' >
				</fieldset><div class="clear"></div>
				<fieldset style="width:48%; float:left; margin-right: 3%;">
					<label>Password</label>
					<input type="password" style="width:92%;" name="password" >
				</fieldset>
				<fieldset style="width:48%; float:left; ">
					<label>Confirm password</label>					
					<input type="password" style="width:92%;" name="confirmPassword" >
				</fieldset><div class="clear"></div>
				<fieldset style="width:48%; float:left; margin-right: 3%;">
					<label>Address</label>
					<input type="text" style="width:92%;" name="address" value='<s:property value="address" />' >
				</fieldset>
				<fieldset style="width:48%; float:left;">
					<label>Email</label>
					<input type="text" style="width:92%;" name="email" value='<s:property value="email" />' >
				</fieldset>
				<fieldset style="width:31%; float:left; margin-right: 3%;"> 
					<label>Phone</label>
					<input type="text" name="phone" style="width:90%;" value='<s:property value="phone" />' >
				</fieldset>
				<fieldset style="width:31%; float:left; margin-right: 3%;"> 
					<label>BirthDay</label>
					<input type="text" name="birthDay" style="width:90%;" placeholder="dd/MM/yyyy" value='<s:property value="birthDay" />' >
				</fieldset>
				<fieldset style="width:31%; float:left;"> 
					<label>Role</label>
				    <select style="width:92%;" name="roleId">
				        <s:iterator value="roles">
				            <option value='<s:property value="id"/>'><s:property value="name"/></option>
				        </s:iterator>
				    </select>	
				</fieldset><div class="clear"></div>
				
		</div>
		
	<footer>
         <s:if test="error != ''">
             <font color="red" style="float:left; margin-left: 3%; padding: 5px 0"><s:property value="error"/></font>
         </s:if>   
		<div class="submit_link">
			<input type="submit" value="Create" class="alt_btn" />
			<input type="button" value="Back" onclick="javascript:history.back(1)" />
		</div>
	</footer>
  </form>
</article><!-- end of post new article -->
</section>