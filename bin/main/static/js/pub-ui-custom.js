/* pub-js */

fetch("/include/header.html")
  .then(response => {
    return response.text()
  })
  .then(data => {
    document.querySelector("#header").innerHTML = data;
  });

fetch("/include/footer.html")
  .then(response => {
    return response.text()
  })
  .then(data => {
    document.querySelector("#footer").innerHTML = data;
  });

window.onload = () => {

  if (document.querySelector('.main-navbar')) {
    document.addEventListener('mouseover', function(event) {
      if (event.target.closest('.main-list-item .nav-hover') || event.target.closest('.main-nav-list-menu')) {
        document.querySelector('.main-nav-list-menu').classList.add('active');
        document.querySelector('.main-nav-list-menu').style.zIndex = 1000;
      } else if (!event.target.closest('.main-navbar')) {
        document.querySelector('.main-nav-list-menu').classList.remove('active');
        document.querySelector('.main-nav-list-menu').style.zIndex = 0;
      }
    });
  }
    
  // 분석
  let modalBackground = document.querySelector('.modal-background');
  console.log(modalBackground);
  const loginView = (e) => {
    if (e == 'open') {
      document.body.style.cssText = `position: fixed; top: -${window.scrollY}px; overflow-y: scroll; width: 100%;`;
      modalBackground.style.display = 'block';
    }
    
    if (e == 'close') {
      email.value = '';
      password.value = '';
      document.body.style.cssText = '';
      window.scrollTo(0, parseInt(document.body.style.top || '0', 10) * -1);
      modalBackground.style.display = 'none';
    }
  }

  const loginChk  = () => {}
  

  document.addEventListener('click', function(event) {
    if (event.target.matches('.xi-bookmark-o') || event.target.matches('.xi-bookmark')) {
      event.target.classList.toggle("xi-bookmark-o");
      event.target.classList.toggle("xi-bookmark");
    }
  });
  
  document.addEventListener('click', function(event) {
    if (event.target.matches('.xi-heart-o') || event.target.matches('.xi-heart')) {
      event.target.classList.toggle("xi-heart-o");
      event.target.classList.toggle("xi-heart");
    }
  });
  
  document.addEventListener('click', function(event) {
    if (event.target.matches('.xi-heart-o') || event.target.matches('.xi-heart')) {
      event.target.classList.toggle("xi-heart-o");
      event.target.classList.toggle("xi-heart");
    }
  });



}


  

