document.addEventListener("DOMContentLoaded", () => {
    document.getElementById("profile-picture").addEventListener("change", () => {
        document.getElementById("upload-picture-form").submit();
    });
});