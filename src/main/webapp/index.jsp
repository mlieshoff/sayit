<%@ page import="sayit.comment.Data" %>
<%@ page import="sayit.comment.Media" %>
<%@ page import="sayit.comment.Post" %>
<%@ page import="sayit.comment.SayItService" %>
<%@ page import="sayit.comment.ShardDao" %>
<%@ page import="sayit.comment.Thread" %>
<%@ page import="sayit.comment.Utils" %>
<%@ page import="system.dao.DaoFactory" %>
<%@ page import="system.service.ServiceFactory" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="org.apache.commons.io.FilenameUtils" %>
<html>

<style>
    html, body, a {
        background: #000000;
        color: #00FF00;
        font-family: monospace;
        height: 100%;
        text-decoration: none;
    }
    a {
        text-decoration: underline;
    }
    .first {
        border: 1px solid green;
    }
    .regular {
    }
    .postimage {
        float: left;
        margin-right: 20px;
        margin-bottom: 20px;
    }
    .postimagelink {
    }
    .postuser {
    }
    .postcomment {
    }
    .postreply {
    }
    .breaker {
        clear: left;
    }
</style>

<script type="text/javascript">
    function toggle(id, action, value) {
        var e = document.getElementById(id);
        document.forms[0].action = action;
        document.getElementById('thread').value = value;
        if (e.style.display == "none") {
            e.style.display = "";
        } else {
            e.style.display = "none";
        }
    }
    function image(url) {
        var e = document.getElementById('image');
        document.getElementById('image_img').src = url;
        if (e.style.display == "none") {
            e.style.display = "";
        } else {
            e.style.display = "none";
        }
    }
</script>

<body>
<center><h1>Post it!</h1></center>

<center>
    <a href="javascript:toggle('form', '/create', '')">
        New Post
    </a>
</center>

<center>
    <div id="image" style="display: none">
        <img id="image_img" onclick="javascript:image('')" src="xxx" />
    </div>
</center>

<center>
    <div id="form" style="display: none">
        <form method="post" action="" enctype="multipart/form-data">
            <table style="width: 50%">
                <tr>
                    <td>User</td>
                    <td style="text-align: left;"><input name="user" id="user" placeholder="anonym" style="width: 40%"/></td>
                </tr>
                <tr>
                    <td>Post</td>
                    <td><textarea name="comment" id="comment" rows="10" style="width: 40%"></textarea></td>
                </tr>
                <tr>
                    <td>File</td>
                    <td><input name="file" type="file" size="50" accept="*" /></td>
                </tr>
                <tr>
                    <td colspan="2">
                        <input type="submit">
                    </td>
                </tr>
            </table>
            <input id="thread" name="thread" type="hidden"/>
        </form>
    </div>
</center>
<%
    ShardDao shardDao = DaoFactory.getDao(ShardDao.class);
    Data data = ServiceFactory.getService(SayItService.class).list();
    Map<Long, Media> medias = data.getMedias();
    for (Map.Entry<Thread, List<Post>> entry : data.getThreads().entrySet()) {
        Thread thread = entry.getKey();
        List<Post> posts = entry.getValue();
        boolean first = true;
        for (Post post : posts) {
            %>
            <p class="<%= first ? "first" : "regular" %>">
            <%
            if (first) {
                first = false;
            }
            boolean hasMedia = medias.containsKey(post.getId());
            if (hasMedia) {
                Media media = medias.get(post.getId());
                String base = "https://" + shardDao.get(media.getShard()).getFolder() + "/";
                String url = base + media.getFilename();
                if (Utils.isImageFileExtension(FilenameUtils.getExtension(media.getFilename()))) {
                    String thumb = base + "thumbnail." + media.getFilename();

                    %>
                        <img class="postimage" src="<%=thumb%>" onclick="javascript:image('<%=url%>')" alt="<%=media.getFilename()%>"/>
                    <%

                } else {
                    %>
                        <a class="postimagelink" href="url"><%=media.getFilename()%></a>
                    <%
                }
            }
            %>
                <span class="postuser"><%=post.getUser()%></span>, <span class="posttimestamp"><%=Utils.formatDateTime(post.getTimestamp())%></span><p/>
                <span class="postcomment"><%=post.getComment()%></span>
                <a class="postreply" href="javascript:toggle('form', '/create', '<%=post.getThread()%>')">
                    Reply
                </a>
            </p>
            <p class="breaker"/>
            <%
        }
    }
%>

<hr/>

</body>

</html>