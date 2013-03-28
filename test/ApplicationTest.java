import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonNode;
import org.junit.*;

import play.mvc.*;
import play.test.*;
import play.data.DynamicForm;
import play.data.validation.ValidationError;
import play.data.validation.Constraints.RequiredValidator;
import play.i18n.Lang;
import play.libs.F;
import play.libs.F.*;

import static play.test.Helpers.*;
import static org.fest.assertions.Assertions.*;


/**
*
* Simple (JUnit) tests that can call all parts of a play app.
* If you are interested in mocking a whole application, see the wiki for more details.
*
*/
public class ApplicationTest
{

    // @Test
    // public void renderTemplate() {
    //     Content html = views.html.index.render("Your new application is ready.");
    //     assertThat(contentType(html)).isEqualTo("text/html");
    //     assertThat(contentAsString(html)).contains("Your new application is ready.");
    // }
    
    @Test
    public void analyzeTrackMissingArtist()
    {
    	Result result = callAction(
    			controllers.routes.ref.Application.analyzeTrack("testTitle", ""));
    	assertThat(status(result)).isEqualTo(BAD_REQUEST);
    	assertThat(contentType(result)).isEqualTo("application/json");
    	assertThat(contentAsString(result)).contains("Missing parameter 'artist'");
    }
    
    @Test
    public void analyzeTrackMissingTitle() 
    {
    	Result result = callAction(
    			controllers.routes.ref.Application.analyzeTrack("", "testArtist"));
    	assertThat(status(result)).isEqualTo(BAD_REQUEST);
    	assertThat(contentType(result)).isEqualTo("application/json");
    	assertThat(contentAsString(result)).contains("Missing parameter 'title'");
    }
    
    @Test
    public void analyzeTrackGood()
    {
    	Result result = callAction(
    			controllers.routes.ref.Application.analyzeTrack("testTitle", "testArtist"));
    	assertThat(status(result)).isEqualTo(OK);
    	assertThat(contentType(result)).isEqualTo("application/json");
    }

}
