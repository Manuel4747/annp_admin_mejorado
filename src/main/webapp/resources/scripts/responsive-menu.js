/**
 * ANNP Admin - Sistema de Menú Responsivo
 * Maneja la funcionalidad del sidebar y el menú hamburguesa
 */

(function() {
    'use strict';

    // Inicializar cuando el DOM esté listo
    document.addEventListener('DOMContentLoaded', function() {
        initResponsiveMenu();
    });

    function initResponsiveMenu() {
        var wrapper = document.querySelector('.layout-wrapper');
        var sidebar = document.querySelector('.layout-sidebar');
        var menuBtn = document.querySelector('#layout-menu-btn');
        var mask = document.querySelector('.layout-main-mask');
        var menuLinks = document.querySelectorAll('.layout-menu a');

        if (!wrapper || !sidebar || !menuBtn) {
            console.warn('Elementos del menú no encontrados');
            return;
        }

        // Toggle del menú en móvil
        menuBtn.addEventListener('click', function(e) {
            e.preventDefault();
            toggleMobileMenu();
        });

        // Cerrar menú al hacer click en la máscara
        if (mask) {
            mask.addEventListener('click', function() {
                closeMobileMenu();
            });
        }

        // Manejar clicks en enlaces del menú
        menuLinks.forEach(function(link) {
            link.addEventListener('click', function(e) {
                var parentLi = this.parentElement;
                var submenu = parentLi.querySelector('ul');

                if (submenu) {
                    e.preventDefault();
                    toggleSubmenu(parentLi, submenu);
                } else if (window.innerWidth <= 1280) {
                    // Cerrar menú en móvil después de seleccionar una opción
                    setTimeout(function() {
                        closeMobileMenu();
                    }, 300);
                }
            });
        });

        // Manejar cambios de tamaño de ventana
        var resizeTimer;
        window.addEventListener('resize', function() {
            clearTimeout(resizeTimer);
            resizeTimer = setTimeout(function() {
                handleResize();
            }, 250);
        });

        // Funciones auxiliares
        function toggleMobileMenu() {
            if (window.innerWidth <= 1280) {
                wrapper.classList.toggle('layout-mobile-active');
                document.body.style.overflow = wrapper.classList.contains('layout-mobile-active') ? 'hidden' : '';
            }
        }

        function closeMobileMenu() {
            wrapper.classList.remove('layout-mobile-active');
            document.body.style.overflow = '';
        }

        function toggleSubmenu(parentLi, submenu) {
            var isActive = parentLi.classList.contains('active-menuitem');
            
            // Cerrar otros submenús del mismo nivel
            var siblings = Array.from(parentLi.parentElement.children);
            siblings.forEach(function(sibling) {
                if (sibling !== parentLi && sibling.classList.contains('active-menuitem')) {
                    sibling.classList.remove('active-menuitem');
                    var siblingSubmenu = sibling.querySelector('ul');
                    if (siblingSubmenu) {
                        slideUp(siblingSubmenu);
                    }
                }
            });

            // Toggle del submenú actual
            if (isActive) {
                parentLi.classList.remove('active-menuitem');
                slideUp(submenu);
            } else {
                parentLi.classList.add('active-menuitem');
                slideDown(submenu);
            }
        }

        function slideDown(element) {
            element.style.display = 'block';
            element.style.height = 'auto';
            var height = element.offsetHeight;
            element.style.height = '0px';
            element.style.overflow = 'hidden';
            element.style.transition = 'height 0.3s ease';
            
            setTimeout(function() {
                element.style.height = height + 'px';
            }, 10);
            
            setTimeout(function() {
                element.style.height = 'auto';
                element.style.overflow = 'visible';
            }, 310);
        }

        function slideUp(element) {
            element.style.height = element.offsetHeight + 'px';
            element.style.overflow = 'hidden';
            element.style.transition = 'height 0.3s ease';
            
            setTimeout(function() {
                element.style.height = '0px';
            }, 10);
            
            setTimeout(function() {
                element.style.display = 'none';
                element.style.height = 'auto';
            }, 310);
        }

        function handleResize() {
            if (window.innerWidth > 1280) {
                closeMobileMenu();
            }
        }
    }

    // Exponer funciones globales si es necesario
    window.ANNPMenu = {
        init: initResponsiveMenu
    };
})();
