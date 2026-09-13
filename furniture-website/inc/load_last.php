<!-- this file initialises includes that should be made after loading the last element -->
<!-- JavaScript files-->
<?php require_once('inc/globals.php'); ?>
<script src="assets/vendor/bootstrap/js/bootstrap.bundle.min.js"></script>
<script src="assets/vendor/waypoints/lib/noframework.waypoints.js"></script>
<script src="assets/vendor/swiper/swiper-bundle.min.js"></script>
<script src="assets/vendor/choices.js/public/assets/scripts/choices.js"></script>
<script src="assets/js/theme.js"></script>
<?php if($pages_assoc_with_name['contact']==$_SERVER["PHP_SELF"]){?>
  <script src="assets/vendor/leaflet/leaflet.js"></script>
  <script src = "assets/js/map.js"></script>
<?php } ?>
<script>
  // ------------------------------------------------------- //
  //   Inject SVG Sprite - 
  //   see more here 
  //   https://css-tricks.com/ajaxing-svg-sprite/
  // ------------------------------------------------------ //
  function injectSvgSprite(path) {
  
      var ajax = new XMLHttpRequest();
      ajax.open("GET", path, true);
      ajax.send();
      ajax.onload = function(e) {
      var div = document.createElement("div");
      div.className = 'd-none';
      div.innerHTML = ajax.responseText;
      document.body.insertBefore(div, document.body.childNodes[0]);
      }
  }
  // this is set to BootstrapTemple website as you cannot 
  // inject local SVG sprite (using only 'icons/orion-svg-sprite.svg' path)
  // while using file:// protocol
  // pls don't forget to change to your domain :)
  injectSvgSprite('https://bootstraptemple.com/files/icons/orion-svg-sprite.svg'); 
  
</script>
<!-- FontAwesome CSS - loading as last, so it doesn't block rendering-->
<link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.7.1/css/all.css" integrity="sha384-fnmOCqbTlWIlj8LyTjo7mOUStjsKC4pOpQbqyi7RrhN7udi9RwhKkMHpvLbHG9Sr" crossorigin="anonymous">