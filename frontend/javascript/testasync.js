const posts = [
    { title: 'Post One', body: 'This is post one!'},
    { title: 'Post One', body: 'This is post one!'}
];

function getPost() {
    setTimeout(() => {
    let output = '';
    posts.forEach((post, index) => {
        output += `<li>${post.title}</li>`;
    });
    document.body.innerHTML = output;
    }, 1000);
}

getPosts();

createPost(post) {
    setTimeout(() => {
        posts.push(post);
    }, 2000);
}