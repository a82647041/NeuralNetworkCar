config = luajava.newInstance("com.mygdx.game.Entity.Config");
--main = luajava.newInstance("com.mygdx.game.MyGdxGame");
main = luajava.bindClass("com.mygdx.game.MyGdxGame")
nn = luajava.bindClass("com.mygdx.game.Entity.NN")
util = luajava.bindClass("com.mygdx.game.Util.MyUtil")
pop = luajava.bindClass("com.mygdx.game.Entity.Population")

--print 'helloworld';

