document.addEventListener("DOMContentLoaded", () => {
    document.querySelectorAll("form.form-delete").forEach(($form) => {
        $form.addEventListener("submit", (ev) => {
            ev.preventDefault();
            const currentModal = document.getElementById("current-modal");
            openModal(currentModal);
            document.getElementById("button-confirm").onclick = () => {
                document.getElementById("button-confirm").onclick = null;
                ev.target.submit();
            }
        })
    })


    document.querySelectorAll('.notification .delete').forEach(($delete) => {
        const $notification = $delete.parentNode;

        $delete.addEventListener('click', () => {
            $notification.parentNode.removeChild($notification);
        });

        setTimeout(() => $notification.parentNode.removeChild($notification), 2000);
    });

    function openModal($el) {
        $el.classList.add('is-active');
    }

    function closeModal($el) {
        $el.classList.remove('is-active');
    }

    function closeAllModals() {
        document.querySelectorAll('.modal').forEach(($modal) => {
            closeModal($modal);
        });
    }

    document.querySelectorAll('.modal-background, .modal-close, .modal-card-head .delete, .modal-card-foot .button').forEach(($close) => {
        const $target = $close.closest('.modal');

        $close.addEventListener('click', () => {
            closeModal($target);
        });
    });

    document.addEventListener('keydown', (event) => {
        if (event.code === 'Escape') {
            closeAllModals();
        }
    })
});
